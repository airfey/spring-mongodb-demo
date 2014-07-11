package com.zandili.demo.mongo.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static javax.tools.JavaFileObject.Kind;

/**
 * @author airfey
 * @name:CommonUtil
 * @version: 1.0.0
 * @company: damai
 * @description: 常用工具类
 * @date: 2012-10-9 pa 18:03<br/>
 * <br/>
 **/

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonUtils implements Serializable {

	private static final long serialVersionUID = 6458428317155311192L;

	private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	public static Map<String, Object> object2Map(Object obj) {

		if (isEmpty(obj))
			return Collections.EMPTY_MAP;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		// 拿到属性器数组
		try {
			PropertyDescriptor[] pds = Introspector.getBeanInfo(obj.getClass())
					.getPropertyDescriptors();
			for (int index = 0; pds.length > 1 && index < pds.length; index++) {
				if (Class.class == pds[index].getPropertyType()
						|| pds[index].getReadMethod() == null) {

					continue;

				}
				Object value = pds[index].getReadMethod().invoke(obj);
				// 只处理简单类型,对于对象类型,集合不处理
				if (notEmpty(value)) {

					if (isPrototype(pds[index].getPropertyType())// java里的原始类型(去除自己定义类型)
							|| pds[index].getPropertyType().isPrimitive()// 基本类型
							|| isPrimitivePackageType(pds[index]
									.getPropertyType())
							|| pds[index].getPropertyType() == String.class) {

						resultMap.put(pds[index].getName(), value);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 非空判断
	 * 
	 * @param obj
	 *            要判断,处理的对象
	 * @return Boolean
	 * @author <a href="mailto:wolfboy@foxmail.com">Ben</a>
	 * @see <b>对象为Null返回true,集合的大小为0也返回true,迭代器没有下一个也返回true..</b>
	 * @since 1.0
	 */
	public static Boolean isEmpty(Object obj) {

		if (obj == null) {
			return Boolean.TRUE;
		}
		// 字符序列集
		if ((obj instanceof CharSequence) && "".equals(obj.toString().trim())) {
			return Boolean.TRUE;
		}
		// 单列集合
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty();
		}
		// 双列集合
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj instanceof Iterable) {
			return ((Iterable<?>) obj).iterator() == null
					|| !((Iterable<?>) obj).iterator().hasNext();
		}

		// 迭代器
		if (obj instanceof Iterator) {
			return !((Iterator<?>) obj).hasNext();
		}

		// 文件类型
		if (obj instanceof File) {
			return !((File) obj).exists();
		}
		// 数组
		return (obj instanceof Object[]) && ((Object[]) obj).length == 0;

	}

	/**
	 * 对象属性拷贝 <b>拷贝两个对象的值，参数一:拷贝源，参数二:拷贝目标</b></br>
	 * <b>拷贝源里为空(或List不能为单size为0)的属性值将不会往目标对象里拷贝
	 * 注意如果传入的两个对象有继承关系,则:第一个参数destObj为子类，第二个参数srcObj为父类。参数顺序不能颠倒! </b>
	 * 
	 * @param destObj
	 *            拷贝目标实例 || 子类
	 * @param srcObj
	 *            拷贝源实例 || 夫类
	 * @return void
	 * @throws Exception
	 * @author airfey
	 * @since 1.0
	 */
	public static void copyProperties(Object destObj, Object srcObj)
			throws Exception {

		if (isEmpty(destObj) || isEmpty(srcObj)) {
			logger.error("srcObj or destObj Must be not Null!");
		}

		// 两个类型一样不执行,否则判断srcObj是否是destObj的父类!
		if (destObj.getClass() != srcObj.getClass()
				&& destObj.getClass().getSuperclass() != srcObj.getClass()) {
			logger.error("srcObj and the destObj don't have the same class or interface, or destObj's super class or super interface is not be this srcObj!");
		}

		// 拿到拷贝源上的属性器数组
		PropertyDescriptor[] srcObjPds = Introspector.getBeanInfo(
				srcObj.getClass()).getPropertyDescriptors();

		for (int i = 0; srcObjPds.length > 1 && i < srcObjPds.length; i++) {

			if (Class.class == srcObjPds[i].getPropertyType()
					|| srcObjPds[i].getReadMethod() == null) {

				continue;
			}

			Object value = srcObjPds[i].getReadMethod().invoke(srcObj);// 拿到拷贝源上的该值
			if (isEmpty(value)) {
				continue;
			}

			// 避免集合浅拷贝(destObj和srcObj引用的是同一个集合)
			if (value instanceof List) {
				value = new ArrayList((List) value);
			} else if (value instanceof Set) {
				value = new LinkedHashSet((Set) value);
			} else if (value instanceof Map) {
				value = new HashMap((Map) value);
			}
			try {
				srcObjPds[i].getWriteMethod().invoke(destObj, value);
			} catch (InvocationTargetException e) {
				// 目标方法执行失败退出本次循环(目标类上没set方法)
				logger.error(e.getMessage());
			}
		}

	}

	/**
	 * 空判断
	 * 
	 * @param obj
	 *            要判断,处理的对象
	 * @return Boolean
	 * @author <a href="mailto:wolfboy@foxmail.com">Ben</a>
	 * @see <b>与非空相反</b>
	 * @since 1.0
	 */
	public static Boolean notEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static Long toLong(Object val, Long defVal) {
		if (isEmpty(val)) {
			return defVal;
		}
		try {
			return Long.parseLong(val.toString());
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public static Long toLong(Object val) {
		return toLong(val, null);
	}

	public static Integer toInt(Object val, Integer defVal) {
		if (isEmpty(val)) {
			return defVal;
		}
		try {
			return Integer.parseInt(val.toString());
		} catch (NumberFormatException e) {
			return defVal;
		}
	}

	public static Integer toInt(Object val) {
		return toInt(val, null);
	}

	/**
	 * 对Null作预处理
	 * 
	 * @param obj
	 *            待处理的对象
	 * @param clazz
	 *            该对象的类型
	 * @return T 返回处理后的不为Null的该对象
	 * @author <a href="mailto:wolfboy@foxmail.com">Ben</a>
	 * @see <b>对Null作预处理,有效避免NullPointerException</b>
	 * @since 1.0
	 */
	public static <T> T preparedNull(T obj, Class<?> clazz) {

		if (notEmpty(obj)) {
			return obj;
		}

		AssertUtils.notNull(clazz, "this class must be not null!");

		Object val = null;

		// 单列集合
		if (List.class.isAssignableFrom(clazz)) {
			val = new ArrayList<Object>(0);
		} else if (Set.class.isAssignableFrom(clazz)) {
			val = new HashSet<Object>(0);
		} else if (Map.class.isAssignableFrom(clazz)) {
			val = new HashMap<Object, Object>(0);
		} else {
			try {
				val = clazz.newInstance();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return (T) val;
	}

	public static List arrayToList(Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	public static boolean contains(Iterator iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Enumeration contains the given element.
	 * 
	 * @param enumeration
	 *            the Enumeration to check
	 * @param element
	 *            the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean contains(Enumeration enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>
	 * Enforces the given instance to be present, rather than returning
	 * <code>true</code> for an equal element as well.
	 * 
	 * @param collection
	 *            the Collection to check
	 * @param element
	 *            the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static boolean containsInstance(Collection collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	public static <A, E extends A> A[] toArray(Enumeration<E> enumeration,
			A[] array) {
		ArrayList<A> elements = new ArrayList<A>();
		while (enumeration.hasMoreElements()) {
			elements.add(enumeration.nextElement());
		}
		return elements.toArray(array);
	}

	/**
	 * Adapt an enumeration to an iterator.
	 * 
	 * @param enumeration
	 *            the enumeration
	 * @return the iterator
	 */
	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		@SuppressWarnings("hiding")
		class EnumerationIterator<E> implements Iterator<E> {
			private Enumeration<E> enumeration;

			public EnumerationIterator(Enumeration<E> enumeration) {
				this.enumeration = enumeration;
			}

			public boolean hasNext() {
				return this.enumeration.hasMoreElements();
			}

			public E next() {
				return this.enumeration.nextElement();
			}

			public void remove() throws UnsupportedOperationException {
				throw new UnsupportedOperationException("Not supported");
			}
		}
		return new EnumerationIterator<E>(enumeration);
	}

	// 获取系统名字
	public static String getOsName() {
		return System.getProperties().getProperty("os.name");
	}

	public static boolean isLinuxOs() {
		return getOsName().toUpperCase().startsWith("LIN");
	}

	// 是否为Window系统
	public static boolean isWindowOs() {
		return getOsName().toUpperCase().startsWith("WIN");
	}

	// 判断类型是否为jdk里自带的原始类型
	public static boolean isPrototype(Class clazz) {
		return clazz.getClassLoader() == null;
	}

	// 判断类型是否为java 8大基本类型的包装类
	public static boolean isPrimitivePackageType(Class clazz) {
		if (isEmpty(clazz) || clazz.isPrimitive()) {
			return Boolean.FALSE;
		}
		return Java8Type.getIndexByObjectTypeName(clazz.getName()) > 0;
	}

	/**
	 * @Title: getGuid
	 * @Description:生成不带符号的Guid字符串
	 * @param:
	 * @return: String
	 * @author: LuoRun
	 * @date: 2012-10-17 下午1:30:10
	 * @throws:
	 */
	public static String getGuid() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	/**
	 * @Title: urlEncoder
	 * @Description:URL字符编码
	 * @param:
	 * @return: String
	 * @author: LuoRun
	 * @date: 2012-10-22 下午12:11:27
	 * @throws:
	 */
	public static String urlEncoder(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title getRandomString
	 * @Description 随机生成字符串
	 * @param length
	 *            =生成字符串的长度
	 * @return String
	 * @author LuoRun
	 * @date 2012-10-22 下午12:13:17
	 */
	public static String getRandomString(Integer length) {
		String base = "abcdefghijkmnpqrstuvwxyz23456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @Title: listToString
	 * @Description:List对像转换为字符串
	 * @param:
	 * @return: String
	 * @author: LuoRun
	 * @date: 2012-10-23 下午8:52:24
	 * @throws:
	 */
	public static String stringListToString(List<String> list, String speater) {
		if (speater.matches("\\\\++")) {
			int len = speater.length();
			for (int i = 0; i < len; i++)
				speater += "\\";
		}
		return list.toString().replaceAll(", ", speater)
				.replaceAll("\\[|]", "");

	}

	public static long getDateDiffSeconds(Date d1, Date d2) {
		return Math.abs(d1.getTime() - d2.getTime()) / 1000;
	}

	public static void copyBeanProperties(Object destObj, Object srcObj)
			throws Exception {
		if (isEmpty(destObj) || isEmpty(srcObj)) {
			logger.error("srcObj or destObj Must be not Null!");
			return;
		}

		// 拿到拷贝源上的属性器数组
		PropertyDescriptor[] srcObjPds = Introspector.getBeanInfo(
				srcObj.getClass()).getPropertyDescriptors();

		PropertyDescriptor[] destObjPds = Introspector.getBeanInfo(
				destObj.getClass()).getPropertyDescriptors();

		for (int i = 0; srcObjPds.length > 1 && i < srcObjPds.length; i++) {
			// 跳出从object继承的class属性,源上必须有get方法
			if (Class.class == srcObjPds[i].getPropertyType()
					|| srcObjPds[i].getReadMethod() == null) {
				continue;
			}

			for (int j = 0; destObjPds.length > 1 && j < destObjPds.length; j++) {
				// 跳出从object继承的class属性,目标类上必须有set方法
				if (Class.class == destObjPds[j].getPropertyType()
						|| destObjPds[j].getWriteMethod() == null) {
					continue;
				}

				// 类型和属性名一样

				if ((srcObjPds[i].getPropertyType() == destObjPds[j]
						.getPropertyType() || checkPackageType(
						srcObjPds[i].getPropertyType(),
						destObjPds[j].getPropertyType()))
						&& srcObjPds[i].getName().equals(
								destObjPds[j].getName())) {

					String propertyName = destObjPds[j].getName();

					if (Map.class.isAssignableFrom(srcObjPds[i]
							.getPropertyType())
							|| Collection.class.isAssignableFrom(srcObjPds[i]
									.getPropertyType())) {

						Type descPropType = destObj.getClass()
								.getDeclaredField(propertyName)
								.getGenericType();
						Type srcPropType = srcObj.getClass()
								.getDeclaredField(propertyName)
								.getGenericType();
						// Map或Collection上没加类型,不做处理
						if (!(descPropType instanceof ParameterizedType)
								|| !(srcPropType instanceof ParameterizedType)) {
							continue;
						}

						ParameterizedType destMapPropPT = (ParameterizedType) descPropType;
						ParameterizedType srcMapPropPT = (ParameterizedType) srcPropType;
						Type descPropKeyType = destMapPropPT
								.getActualTypeArguments()[0];
						Type srcPropKeyType = srcMapPropPT
								.getActualTypeArguments()[0];

						if (descPropKeyType != srcPropKeyType)
							continue;

						if (Map.class.isAssignableFrom(srcObjPds[i]
								.getPropertyType())) {
							Type descMapValType = destMapPropPT
									.getActualTypeArguments()[1];
							Type srcMapValKeyType = srcMapPropPT
									.getActualTypeArguments()[1];
							if (descMapValType != srcMapValKeyType)
								continue;
						}
					}

					Object value = srcObjPds[i].getReadMethod().invoke(srcObj);// 拿到拷贝源上的该值
																				// 、
					if (value != null) {
						try {
							destObjPds[j].getWriteMethod().invoke(destObj,
									value);
						} catch (InvocationTargetException e) {
							// 执行目标异常,让目标对象执行set方法,如果出异常则直接退出本次循环
							// skip
						}
					}
				}
			}
		}
	}

	/**
	 * 检查第一个(第二个)参数类型是否为第二个参数个(第一个)类型的包装类
	 * 
	 * @param objType1
	 * @param objType2
	 * @return
	 */
	public static boolean checkPackageType(Class<?> objType1, Class<?> objType2) {
		if (isEmpty(objType1) || isEmpty(objType2)
				|| (objType1.isPrimitive() && objType2.isPrimitive())
				|| (!objType1.isPrimitive() && !objType2.isPrimitive())) {
			return Boolean.FALSE;
		}

		try {
			if (objType1.isPrimitive())
				return objType2.getField("TYPE").get(null) == objType1;
			return objType1.getField("TYPE").get(null) == objType2;
		} catch (Exception e) {
		}
		return Boolean.FALSE;
	}

	// 是否为数字或集合
	public static boolean isArrayOrSet(Object object) {
		if (object instanceof Class) {
			return ((Class) object).isArray()
					|| Collection.class.isAssignableFrom((Class) object);
		}
		return object.getClass().isArray()
				|| Collection.class.isAssignableFrom(object.getClass());
	}

	public static List<String> getClassName(String packageName)
			throws FileNotFoundException {
		List<String> classNames = new ArrayList<String>(0);
		getClassName(packageName, classNames);
		return classNames;
	}

	public static void getClassName(String packageName, List<String> classNames)
			throws FileNotFoundException {
		try {
			String resourceName = packageName.replaceAll("\\.", "/");
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource(resourceName);
			if (url == null) {
				throw new FileNotFoundException();
			}
			if ("jar".equals(url.getProtocol())) {
				String jarPath = url.getPath()
						.substring(0, url.getPath().indexOf("!"))
						.replaceFirst("file:/", "").replaceAll("%20", " ");
				JarFile jarFile = new JarFile(new File(jarPath));
				Enumeration<JarEntry> es = jarFile.entries();
				while (es.hasMoreElements()) {
					String name = null;
					for (int i = 0; i <= packageName.split("\\.").length; i++) {
						if (es.hasMoreElements())
							name = es.nextElement().getName();
						else
							break;
						if (name.contains(Kind.CLASS.toString()))
							break;
					}
					if (name != null && name.endsWith(Kind.CLASS.toString())) {
						classNames.add(name.replaceAll("/", ".").replace(
								Kind.CLASS.toString(), Kind.OTHER.toString()));
					}
				}
			} else {
				File urlFile = new File(url.toURI());
				for (File pkgFile : urlFile.listFiles()) {
					if (pkgFile.isFile()) {
						classNames.add(packageName
								+ "."
								+ pkgFile.getName().replace(
										Kind.CLASS.toString(),
										Kind.OTHER.toString()));
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将String数组转化为Long数组
	 * 
	 * @Title: strArr2LongArr
	 * @param strArr
	 *            String数组
	 * @return Long数组
	 * @author: 鄢华健 2012-12-13上午10:15:42
	 */
	public static Long[] stringArray2LongArray(String[] strArr) {
		if (CommonUtils.isEmpty(strArr)) {
			return null;
		}
		Long longArray[] = new Long[strArr.length];
		for (int i = 0; i < longArray.length; i++) {
			longArray[i] = StringUtils.parseLong(strArr[i]);
		}
		return longArray;
	}

	/**
	 * 将将String数组转化为LongList
	 * 
	 * @Title: strArr2LongList
	 * @param strArr
	 *            String数组
	 * @return LongList
	 * @author: 鄢华健 2012-12-13上午11:09:10
	 */
	public static List<Long> stringArray2LongList(String[] strArr) {
		// 将String数组转化为Long数组
		Long[] longArr = stringArray2LongArray(strArr);
		return longArr == null ? null : Arrays.asList(longArr);
	}

	/**
	 * double类型四舍五入算法
	 * 
	 * @Title: round4
	 * @param v
	 *            double类型数值
	 * @param scale
	 *            取得位数
	 * @return
	 * @author: 鄢华健 2012-12-18上午10:04:43
	 */
	public static double round4(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {
		System.out
				.println(getMD5Str32("7F912011-D975-46C1-A731-90AE0A16185Chelloabc"));
	}

	public static String getMD5Str32(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuilder md5StrBuff = new StringBuilder();

		for (byte aByteArray : byteArray) {
			if (Integer.toHexString(0xFF & aByteArray).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & aByteArray));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
		}
		return md5StrBuff.toString();
	}

	enum Java8Type {
		BYTE(1, "byte", "java.lang.Byte"), INT(2, "int", "java.lang.Integer"), SHORT(
				3, "short", "java.lang.Short"), LONG(4, "long",
				"java.lang.Long"), FLOAT(5, "float", "java.lang.Float"), DOUBLE(
				6, "double", "java.lang.Double"), BOOLEAN(7, "boolean",
				"java.lang.Boolean"), CHAR(8, "char", "java.lang.Character");

		private int index;
		private String primitiveTypeName;
		private String objectTypeName;

		Java8Type(int index, String primitiveTypeName, String objectTypeName) {
			this.index = index;
			this.primitiveTypeName = primitiveTypeName;
			this.objectTypeName = objectTypeName;
		}

		public static int getIndexByPrimitiveTypeName(String primitiveTypeName) {
			for (Java8Type javaLang8Type : Java8Type.values()) {
				if (javaLang8Type.getPrimitiveTypeName().equals(
						primitiveTypeName)) {
					return javaLang8Type.getIndex();
				}
			}
			return -1;
		}

		public static int getIndexByObjectTypeName(String objectTypeName) {
			for (Java8Type jva8Type : Java8Type.values()) {
				if (jva8Type.getObjectTypeName().equals(objectTypeName)) {
					return jva8Type.getIndex();
				}
			}
			return 0;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getPrimitiveTypeName() {
			return primitiveTypeName;
		}

		public void setPrimitiveTypeName(String primitiveTypeName) {
			this.primitiveTypeName = primitiveTypeName;
		}

		public String getObjectTypeName() {
			return objectTypeName;
		}

		public void setObjectTypeName(String objectTypeName) {
			this.objectTypeName = objectTypeName;
		}

	}
}

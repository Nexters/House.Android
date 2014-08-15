package com.nexters.house.utils;

import java.lang.reflect.Method;

public class BeanUtils {

	public static String getBeanGetValue(Object obj) throws Exception {

		StringBuffer temp = new StringBuffer();

		Class cls = obj.getClass();

		Method[] method = cls.getMethods();
		String methodName = "";

		for (int i = 0; i < method.length; i++) {

			methodName = method[i].getName();
			if (methodName.indexOf("get") == 0) {

				String fName = methodName.substring(
						methodName.indexOf("get") + 3).toLowerCase();

				String fValue = "";
				Object fValueObj = method[i].invoke(obj, null);

				if (fValueObj == null) {
					fValue = "null";
				} else {
					fValue = fValueObj.toString();
				}
				temp.append(fName + " = " + fValue + "\n");
			}
		}

		return temp.toString();

	}

}

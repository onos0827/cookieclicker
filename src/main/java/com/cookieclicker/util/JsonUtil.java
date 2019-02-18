package com.cookieclicker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	/*
	 * javaオブジェクトをJSON
	 * 文字列に変換する
	 *
	 */

	public static <Obj> String toJsonString(Obj obj) {

        ObjectMapper mapper = new ObjectMapper();
        String JsonStr = null;
        try {
        	JsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			JsonStr = "{}";
		}

		return JsonStr;

	}

}

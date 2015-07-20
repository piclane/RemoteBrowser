package hina.remotebrowser.server.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クエリ文字列から直接パラメーターを取得します
 * 
 * @author yohei_hina
 */
public class QueryStringParams extends RequestParams {
	/**
	 * クエリマップ
	 * Key  : パラメータ名
	 * Value: パラメータの配列
	 */
	private Map<String, List<String>> params = new HashMap<>();
	
	/**
	 * コンストラクタ
	 * 
	 * @param url URL
	 */
	public QueryStringParams(URL url) {
		this(url.getQuery());
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param queryString クエリ文字列
	 */
	public QueryStringParams(String queryString) {
        try {
			for (String param : queryString.split("&")) {
			    String pair[] = param.split("=");
			    String key = URLDecoder.decode(pair[0], "UTF-8");
			    String value = "";
			    if (pair.length > 1) {
			        value = URLDecoder.decode(pair[1], "UTF-8");
			    }
			    List<String> values = params.get(key);
			    if (values == null) {
			        values = new ArrayList<String>();
			        params.put(key, values);
			    }
			    values.add(value);
			}
		} catch (UnsupportedEncodingException e) {
			throw new InternalError(e);
		}
	}
	
	/**
	 * @see hina.remotebrowser.server.utils.RequestParams#getRawParameter(java.lang.String)
	 */
	@Override
	public String getRawParameter(String name) {
		if(params.containsKey(name)) {
			List<String> param = params.get(name);
			return String.join(",", param);
		}
		return null;
	}

}

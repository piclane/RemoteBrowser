package hina.remotebrowser.server.utils;

import hina.remotebrowser.server.BadRequestException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

/**
 * リクエストパラメーターを処理するクラス
 * 
 * @author yohei_hina
 */
abstract class RequestParams {
	/** {@link Integer} に変換するパーサー */
	public static final RequestParamParser<Integer> INTEGER_PARSER = new RequestParamParser<Integer>() {
		@Override
		public Integer parse(String value) throws Exception {
			if("null".equals(value)) {
				return null;
			}
			return Integer.parseInt(value);
		}
	};
	
	/** コンマセパレーター */
	private static final Pattern COMMA_SEPARATOR = Pattern.compile("\\s*,\\s*");
	
	/** 配列として区切る為の正規表現 */
	private Pattern separator = COMMA_SEPARATOR;
	
	/**
	 * パラメーターを文字列として取得します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	public String getString(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return value;
		}
		throw new BadRequestException("パラメータ " + name + " は有効な文字列である必要があります。");
	}
	
	/**
	 * パラメーターを文字列として取得します
	 * 
	 * @param name パラメーター名
	 * @param def パラメーターが有効な値ではなかった時のデフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	public String getString(String name, String def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return value;
		}
		return def;
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	public String[] getStringArray(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return separator.split(value);
		}
		throw new BadRequestException("パラメータ " + name + " は有効な文字列である必要があります。");
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	public String[] getStringArray(String name, String[] def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return separator.split(value);
		}
		return def != null ? Arrays.copyOf(def, def.length, String[].class) : null;
	}
	
	/**
	 * パラメーターをブール値として取得します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な値では無い場合
	 */
	public boolean getBoolean(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return 
				"1".equals(value) || 
				"true".equalsIgnoreCase(value) || 
				"y".equalsIgnoreCase(value) || 
				"yes".equalsIgnoreCase(value);
		}
		throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。");
	}
	
	/**
	 * パラメーターをブール値として取得します
	 * 
	 * @param name パラメーター名
	 * @param def パラメーターが有効な値ではなかった時のデフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な値では無い場合
	 */
	public boolean getBoolean(String name, boolean def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			return 
				"1".equals(value) || 
				"true".equalsIgnoreCase(value) || 
				"y".equalsIgnoreCase(value) || 
				"yes".equalsIgnoreCase(value);
		}
		return def;
	}
	
	/**
	 * パラメーターを数値として取得します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public int getInt(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
		}
		throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。");
	}
	
	/**
	 * パラメーターを数値として取得します
	 * 
	 * @param name パラメーター名
	 * @param def パラメーターが有効な値ではなかった時のデフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public int getInt(String name, int def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
		}
		return def;
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public int[] getIntArray(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			String[] _values = separator.split(value);
			int len = _values.length;
			int[] values = new int[len];
			try {
				for(int i=0; i<len; i++) {
					values[i] = Integer.parseInt(_values[i]);
				}
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
			return values;
		}
		throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。");
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public int[] getIntArray(String name, int[] def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			String[] _values = separator.split(value);
			int len = _values.length;
			int[] values = new int[len];
			try {
				for(int i=0; i<len; i++) {
					values[i] = Integer.parseInt(_values[i]);
				}
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
			return values;
		}
		return def != null ? Arrays.copyOf(def, def.length) : null;
	}
	
	/**
	 * パラメーターを数値として取得します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public long getLong(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
		}
		throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。");
	}
	
	/**
	 * パラメーターを数値として取得します
	 * 
	 * @param name パラメーター名
	 * @param def パラメーターが有効な値ではなかった時のデフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public long getLong(String name, long def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
		}
		return def;
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public long[] getLongArray(String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			String[] _values = separator.split(value);
			int len = _values.length;
			long[] values = new long[len];
			try {
				for(int i=0; i<len; i++) {
					values[i] = Long.parseLong(_values[i]);
				}
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
			return values;
		}
		throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。");
	}
	
	/**
	 * パラメーターを文字列の配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な数値では無い場合
	 */
	public long[] getLongArray(String name, long[] def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			String[] _values = separator.split(value);
			int len = _values.length;
			long[] values = new long[len];
			try {
				for(int i=0; i<len; i++) {
					values[i] = Long.parseLong(_values[i]);
				}
			} catch (NumberFormatException e) {
				throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。", e);
			}
			return values;
		}
		return def != null ? Arrays.copyOf(def, def.length) : null;
	}
	
	/**
	 * パラメーターを任意のオブジェクトとして取得します
	 * 
	 * @param parser 文字列をオブジェクトに変換する {@link RequestParamParser}
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な値では無い場合
	 */
	public <T> T getObject(RequestParamParser<T> parser, String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return parser.parse(value);
			} catch (Exception e) {
				throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。", e);
			}
		}
		throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。");
	}
	
	/**
	 * パラメーターを任意のオブジェクトとして取得します
	 * 
	 * @param parser 文字列をオブジェクトに変換する {@link RequestParamParser}
	 * @param def パラメーターが有効な値ではなかった時のデフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な値では無い場合
	 */
	public <T> T getObject(RequestParamParser<T> parser, String name, T def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				return parser.parse(value);
			} catch (Exception e) {
				throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。", e);
			}
		}
		return def;
	}
	
	/**
	 * パラメーターを任意のオブジェクトの配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param parser 文字列をオブジェクトに変換する {@link RequestParamParser}
	 * @param cls 配列のクラス
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	@SuppressWarnings("unchecked")
	public <T, S> T[] getObjectArray(RequestParamParser<T> parser, Class<? extends S[]> cls, String name) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				String[] _values = separator.split(value);
				int len = _values.length;
				T[] values = (T[])Array.newInstance(cls.getComponentType(), len);
				for(int i=0; i<len; i++) {
					values[i] = parser.parse(_values[i]);
				}
				return values;
			} catch (Exception e) {
				throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。", e);
			}
		}
		throw new BadRequestException("パラメータ " + name + " は有効な数値である必要があります。");
	}
	
	/**
	 * パラメーターを任意のオブジェクトの配列として取得します
	 * パラメーターの文字列はコンマ区切りである必要があります
	 * 
	 * @param parser 文字列をオブジェクトに変換する {@link RequestParamParser}
	 * @param cls 配列のクラス
	 * @param name パラメーター名
	 * @param def デフォルト値
	 * @return 指定されたパラメーター名の値
	 * @throws ServletException パラメーターが有効な文字列では無い場合
	 */
	@SuppressWarnings("unchecked")
	public <T, S> T[] getObjectArray(RequestParamParser<T> parser, Class<? extends S[]> cls, String name, T[] def) throws ServletException {
		String value = getParameter(name);
		if(value != null) {
			try {
				String[] _values = separator.split(value);
				int len = _values.length;
				T[] values = (T[])Array.newInstance(cls.getComponentType(), len);
				for(int i=0; i<len; i++) {
					values[i] = parser.parse(_values[i]);
				}
				return values;
			} catch (Exception e) {
				throw new BadRequestException("パラメータ " + name + " は有効な値である必要があります。", e);
			}
		}
		return def != null ? (T[])Arrays.copyOf(def, def.length, cls) : null;
	}
	
	/**
	 * {@link ServletRequest} からのパラメーターがあるかどうかを取得します。
	 * パラメーターが存在しない場合、空文字列だった場合は false を返します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーターが存在する時 true
	 */
	public boolean hasParameter(String name) {
		return getParameter(name) != null;
	}
	
	/**
	 * {@link ServletRequest} からパラメーターを取得します。
	 * パラメーターが存在しない場合、空文字列だった場合は null を返します
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 */
	public String getParameter(String name) {
		String param = getRawParameter(name);
		if(param == null) {
			return null;
		}
		
		param = param.trim();
		if(param.isEmpty()) {
			return null;
		}
		
		return param;
	}
	
	/**
	 * {@link ServletRequest} からパラメーターを取得します。
	 * {@link ServletRequest} から生の値を返す点で {@link #getParameter(String)} と異なります。
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 */
	public abstract String getRawParameter(String name);
	
	/**
	 * 配列として区切る為の正規表現を取得します
	 *
	 * @return 配列として区切る為の正規表現
	 */
	public Pattern getSeparator() {
		return separator;
	}

	/**
	 * 配列として区切る為の正規表現を設定します
	 * 
	 * @param separator 配列として区切る為の正規表現
	 */
	public void setSeparator(Pattern separator) {
		if(separator == null) {
			throw new NullPointerException();
		}
		
		this.separator = separator;
	}
}

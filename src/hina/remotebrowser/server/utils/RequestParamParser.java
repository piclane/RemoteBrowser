package hina.remotebrowser.server.utils;

/**
 * 文字列を任意のオブジェクトに変換する為のインターフェイス
 */
@FunctionalInterface
public interface RequestParamParser<T> {
	/**
	 * 文字列を任意のオブジェクトに変換します
	 * 
	 * @param value 文字列
	 * @return 変換されたオブジェクト
	 * @throws Exception 変換時に例外が発生した場合
	 */
	T parse(String value) throws Exception;
}
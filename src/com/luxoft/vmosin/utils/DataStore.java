package com.luxoft.vmosin.utils;

import java.util.List;

public interface DataStore<T> {

	void saveData(List<T> list, String fileName);

	String loadData(String fileName, List<T> list);

}

package com.luxoft.vmosin.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;

public class JsonDataStoreImpl implements DataStore<PersonGroup> {

	private static JsonDataStoreImpl instance;

	private JsonDataStoreImpl() {
	}

	public static JsonDataStoreImpl getInstance() {
		if (instance == null) {
			instance = new JsonDataStoreImpl();
		}
		return instance;
	}

	@Override
	public void saveData(List<PersonGroup> list, String fileName) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject jObj = new JSONObject();
			jObj.put("parent", list.get(i).getParent());
			jObj.put("name", list.get(i).getName());
			JSONArray jsonPersonArray = new JSONArray();
			Person[] persons = (Person[]) list.get(i).getPersons();
			for (int j = 0; j < persons.length; j++) {
				JSONObject person = new JSONObject();
				person.put("name", persons[j].getName());
				person.put("group", persons[j].getParent());
				person.put("address", persons[j].getAddress());
				person.put("city", persons[j].getCity());
				person.put("result", persons[j].getResult());
				jsonPersonArray.put(person);
			}
			jObj.put("persons", jsonPersonArray);
			jsonArray.put(jObj);
		}
		try (FileWriter file = new FileWriter(fileName)) {
			file.write(jsonArray.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@Override
	public String loadData(String fileName, List<PersonGroup> list) {
		String inputData = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			StringBuilder stringBuilder = new StringBuilder();
			while ((inputData = reader.readLine()) != null) {
				stringBuilder.append(inputData);
			}
			inputData = stringBuilder.toString();
		} catch (IOException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
		list.clear();
		JSONArray jsonArray = new JSONArray(inputData);
		PersonGroup groupParent = new PersonGroup(null, "Folder");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jObj = jsonArray.getJSONObject(i);
			PersonGroup group = new PersonGroup(groupParent, jObj.getString("name"));
			JSONArray jsonPersonArray = new JSONArray(jObj.getString("persons"));
			for (int j = 0; j < jsonPersonArray.length(); j++) {
				JSONObject personObj = jsonArray.getJSONObject(i);
				Person person = new Person(personObj.getString("name"), group, personObj.getString("address"),
						personObj.getString("city"), Integer.parseInt(personObj.getString("result")));
				group.addPerson(person);
			}

		}
		return fileName;
	}

}

package com.luxoft.vmosin.handlers;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONObject;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.parts.TreeGroupView;
import com.luxoft.vmosin.utils.Const;

public class SaveHandler {

	@CanExecute
	public boolean canExecute(EPartService partService) {
		if (partService != null) {
			return partService.findPart(Const.PART_TREE_VIEW).isDirty();
		}
		return false;
	}

	@Execute
	public static void execute(EPartService partService) {
		PersonGroup personGroup = ((TreeGroupView) partService.findPart(Const.PART_TREE_VIEW).getObject()).getRoot();
		PersonAbstr[] objArray = personGroup.getPersons();
		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.json", "*.*" });
		String fileName = dialog.open();
		JSONArray jsonArr = new JSONArray();
		for (int i = 0; i < objArray.length; i++) {
			JSONObject jsonRoot = new JSONObject();
			jsonRoot.put("parent", objArray[i].getParent());
			jsonRoot.put("name", objArray[i].getName());
			JSONArray jsonGroupArr = new JSONArray();
			PersonAbstr[] pGroup = ((PersonGroup) objArray[i]).getPersons();
			for (int n = 0; n < pGroup.length; n++) {
				JSONObject jsonGroup = new JSONObject();
				jsonGroup.put("parent", pGroup[n].getParent());
				jsonGroup.put("name", pGroup[n].getName());
				JSONArray jsonPersonArr = new JSONArray();
				PersonAbstr[] persons = ((PersonGroup) pGroup[n]).getPersons();
				for (int j = 0; j < persons.length; j++) {
					JSONObject jsonPerson = new JSONObject();
					jsonPerson.put("name", ((Person) persons[j]).getName());
					jsonPerson.put("group", ((Person) persons[j]).getParent());
					jsonPerson.put("address", ((Person) persons[j]).getAddress());
					jsonPerson.put("city", ((Person) persons[j]).getCity());
					jsonPerson.put("photo", ((Person) persons[j]).getPhotoName());
					jsonPerson.put("result", ((Person) persons[j]).getResult());
					jsonPersonArr.put(jsonPerson);
				}
				jsonGroup.put("persons", jsonPersonArr);
				jsonGroupArr.put(jsonGroup);
			}
			jsonRoot.put("groups", jsonGroupArr);
			jsonArr.put(jsonRoot);
		}
		try (FileWriter file = new FileWriter(fileName)) {
			file.write(jsonArr.toString());
			partService.saveAll(false);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
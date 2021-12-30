package com.luxoft.vmosin.handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONArray;
import org.json.JSONObject;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.parts.TreeGroupView;
import com.luxoft.vmosin.utils.Const;

public class OpenHandler {

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application) {
		FileDialog dialog = new FileDialog(new Shell());
		dialog.setFilterExtensions(new String[] { "*.json", "*.*" });
		String fn = dialog.open();
		if (fn != null) {
			String inputData = null;
			try (BufferedReader reader = new BufferedReader(new FileReader(fn))) {
				StringBuilder stringBuilder = new StringBuilder();
				while ((inputData = reader.readLine()) != null) {
					stringBuilder.append(inputData);
				}
				inputData = stringBuilder.toString();
			} catch (IOException e) {
				System.out.println("File not found.");
				e.printStackTrace();
			}
			PersonGroup newRoot = new PersonGroup(null, Const.TREE_ROOT);
			JSONArray jsonArray = new JSONArray(inputData);
			MPart treeGroupView = partService.findPart(Const.PART_TREE_VIEW);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject rootObj = jsonArray.getJSONObject(i);
				PersonGroup folder = new PersonGroup(newRoot, rootObj.getString("name"));
				newRoot.addPerson(folder);
				JSONArray jsonGroupArray = rootObj.getJSONArray("groups");
				for (int j = 0; j < jsonGroupArray.length(); j++) {
					JSONObject groupObj = jsonGroupArray.getJSONObject(j);
					PersonGroup groups = new PersonGroup(folder, groupObj.getString("name"));
					folder.addPerson(groups);
					JSONArray jsonPersonArray = groupObj.getJSONArray("persons");
					for (int n = 0; n < jsonPersonArray.length(); n++) {
						JSONObject personObj = jsonPersonArray.getJSONObject(n);
						Person pesron = new Person(personObj.getString("name"), groups, personObj.getString("address"),
								personObj.getString("city"), personObj.getString("photo"), personObj.getInt("result"));
						groups.addPerson(pesron);
					}
				}
			}

			// попытка закрыть оставшиеся вкладки в редакторе после загрузки нового файла
//			MPartStack stack = (MPartStack) modelService.find(Const.PART_STACK_EDITOR, application);
//			stack.getChildren().clear();
//			for (int i = 0; i < stack.getChildren().size(); i++) {
//				partService.hidePart((MPart) stack.getChildren().get(i), true);
////				((MPart) stack.getChildren().get(i)).setObject(null);
//			}

			((TreeGroupView) treeGroupView.getObject()).setRoot(newRoot);
			((TreeGroupView) partService.findPart(Const.PART_TREE_VIEW).getObject()).getTreeViewer().setInput(newRoot);
			treeGroupView.setDirty(false);

		}
	}
}

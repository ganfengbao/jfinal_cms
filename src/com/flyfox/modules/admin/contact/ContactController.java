package com.flyfox.modules.admin.contact;

import com.flyfox.jfinal.base.BaseController;
import com.flyfox.jfinal.component.annotation.ControllerBind;
import com.flyfox.jfinal.component.db.SQLUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * 联系人管理
 * 
 * @author flyfox 2014-2-11
 */
@ControllerBind(controllerKey = "/admin/contact")
public class ContactController extends BaseController {

	private static final String path = "/pages/admin/contact/contact_";

	public void list() {
		TbContact model = getModelByAttr(TbContact.class);

		SQLUtils sql = new SQLUtils(" from tb_contact t where 1=1 ");
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			sql.whereLike("name", model.getStr("name"));
			sql.whereEquals("type", model.getStr("type"));
		}

		Page<TbContact> page = TbContact.dao.paginate(getPaginator(), "select t.* ", //
				sql.toString().toString());

		// 下拉框
		setAttr("page", page);
		setAttr("attr", model);
		render(path + "list.html");
	}

	public void add() {
		render(path + "add.html");
	}

	public void view() {
		TbContact model = TbContact.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "view.html");
	}

	public void delete() {
		TbContact model = new TbContact();
		Integer userid= getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);
		model.deleteById(getParaToInt());
		
		list();
	}

	public void edit() {
		TbContact model = TbContact.dao.findById(getParaToInt());
		setAttr("model", model);
		render(path + "edit.html");
	}

	public void save() {
		Integer pid = getParaToInt();
		TbContact model = getModel(TbContact.class);
		
		
		Integer userid= getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);
		if (pid != null && pid > 0) { // 更新
			model.update();
		} else { // 新增
			model.remove("id");
			model.put("create_id", userid);
			model.put("create_time", now);
			model.save();
		}
		renderMessage("保存成功");
	}
}
CREATE TABLE IF NOT EXISTS sys_user_key (id varchar(40) NOT NULL COMMENT 'ID', userAccount varchar(100) NOT NULL COMMENT '用户帐号', accessKey varchar(50) NOT NULL COMMENT '访问Key', secretKey varchar(100) NOT NULL COMMENT '用户密钥', timeout INT DEFAULT 0 COMMENT '过期时间', isEnabled Boolean COMMENT '是否挂起', PRIMARY KEY (id), Unique(userAccount)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_auditlog(id varchar(40) NOT NULL COMMENT 'ID', operationType varchar(50) COMMENT '操作类型', operatorName varchar(50) COMMENT '操作人姓名', preValue text COMMENT '旧值', curValue text COMMENT '新值', operationTime datetime COMMENT '操作时间', operationClass varchar(500) COMMENT '操作类', operationClassID varchar(50) COMMENT '记录ID', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_auditlog_history_2015 (id varchar(40) NOT NULL COMMENT 'ID', operationType varchar(50) COMMENT '操作类型', operatorName varchar(50) COMMENT '操作人姓名', preValue text COMMENT '旧值', curValue text COMMENT '新值', operationTime datetime COMMENT '操作时间', operationClass varchar(500) COMMENT '操作类', operationClassID varchar(50) COMMENT '记录ID', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_auditlog_history_2016 (id varchar(40) NOT NULL COMMENT 'ID', operationType varchar(50) COMMENT '操作类型', operatorName varchar(50) COMMENT '操作人姓名', preValue text COMMENT '旧值', curValue text COMMENT '新值', operationTime datetime COMMENT '操作时间', operationClass varchar(500) COMMENT '操作类', operationClassID varchar(50) COMMENT '记录ID', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_dic_data (id varchar(40) NOT NULL, name varchar(60) NOT NULL COMMENT '名称', code varchar(60) COMMENT '编码', pid varchar(50) COMMENT '父ID', seq int COMMENT '排序', remark varchar(2000) COMMENT '描述', state Boolean DEFAULT false COMMENT '是否有效', typekey varchar(20) COMMENT '类型', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('16b80bfb-f0ee-47a0-ba94-cc256abaed11', '学历', '', null, null, '', true, 'xueli');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('16b80bfb-f0ee-47a0-ba94-cc256abaed12', '民族', '', null, null, '', true, 'minzu');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('16b80bfb-f0ee-47a0-ba94-cc256abaed13', '级别', '', null, null, '', true, 'grade');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('d7d1744b-e69f-48d0-9760-b2eae6af039b', '本科', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed11', 1, '', true, 'xueli');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('16b80bfb-f0ee-47a0-ba94-cc256abaed17', '专科', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed11', 2, '', true, 'xueli');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('7ed23330-5538-4943-8678-0c5a2121cf57', '高中', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed11', 3, '', true, 'xueli');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('936db407-ae1-45a7-a657-b60580e2a77a', '汉族', '101', '16b80bfb-f0ee-47a0-ba94-cc256abaed12', 1, '', true, 'minzu');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('936db407-ae2-45a7-a657-b60580e2a77a', '回族', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed12', 2, '', true, 'minzu');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('936db407-ae3-45a7-a657-b60580e2a77a', '一级', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed13', 1, '', true, 'grade');
INSERT INTO sys_dic_data (id, name, code, pid, seq, remark, state, typekey) VALUES ('936db407-ae4-45a7-a657-b60580e2a77a', '二级', '', '16b80bfb-f0ee-47a0-ba94-cc256abaed13', 2, '', true, 'grade');

-- CREATE TABLE IF NOT EXISTS sys_conf (id varchar(100) NOT NULL COMMENT 'ID', pkey varchar(50) COMMENT '参数Key', pvalue varchar(100) COMMENT '参数键值', comment varchar(300) COMMENT '备注',deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_fwlog (id varchar(100) NOT NULL COMMENT 'ID', startDate datetime COMMENT '访问时间', strDate varchar(100) COMMENT '访问时间(毫秒)', tomcat varchar(50) COMMENT 'Tomcat', userCode varchar(300) COMMENT '登陆账号', userName varchar(200) COMMENT '姓名', sessionId varchar(300) COMMENT 'sessionId', ip varchar(200) COMMENT 'IP', fwUrl varchar(3000) COMMENT '访问菜单', menuName varchar(100) COMMENT '菜单名称', isqx varchar(2) COMMENT '是否有权限访问', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_fwlog_history_2015 (id varchar(100) NOT NULL COMMENT 'ID', startDate datetime COMMENT '访问时间', strDate varchar(100) COMMENT '访问时间(毫秒)', tomcat varchar(50) COMMENT 'Tomcat', userCode varchar(300) COMMENT '登陆账号', userName varchar(200) COMMENT '姓名', sessionId varchar(300) COMMENT 'sessionId', ip varchar(200) COMMENT 'IP', fwUrl varchar(3000) COMMENT '访问菜单', menuName varchar(100) COMMENT '菜单名称', isqx varchar(2) COMMENT '是否有权限访问', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_fwlog_history_2016 (id varchar(100) NOT NULL COMMENT 'ID', startDate datetime COMMENT '访问时间', strDate varchar(100) COMMENT '访问时间(毫秒)', tomcat varchar(50) COMMENT 'Tomcat', userCode varchar(300) COMMENT '登陆账号', userName varchar(200) COMMENT '姓名', sessionId varchar(300) COMMENT 'sessionId', ip varchar(200) COMMENT 'IP', fwUrl varchar(3000) COMMENT '访问菜单', menuName varchar(100) COMMENT '菜单名称', isqx varchar(2) COMMENT '是否有权限访问', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS sys_menu (id varchar(40) NOT NULL, pluginId varchar(40), name varchar(60), pid varchar(40), description varchar(2000) COMMENT '描述', pageurl varchar(3000), type int COMMENT '0.功能按钮,1.导航菜单, 2.插件', state Boolean DEFAULT true COMMENT '是否有效', seq int, icon varchar(100), deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('business_manager', '业务管理', null, null, null, 1, true, 1, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('system_manager', '系统管理', null, null, null, 1, true, 3, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_plugin_list', '插件管理', 'system_manager', null, 'system/plugin/list', 1, true, 1, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_plugin_update', '修改插件', 'sys_plugin_list', null, '/system/plugin/update', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_plugin_delete', '删除插件', 'sys_plugin_list', null, '/system/plugin/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_plugin_enable', '启用/停用插件', 'sys_plugin_list', null, '/system/plugin/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_plugin_mount', '设置插件挂载点', 'sys_plugin_list', null, '/system/plugin/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_auditlog_list', '修改日志', 'system_manager', null, '/system/auditlog/list', 1, true, 2, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_auditlog_look', '查看修改日志', 'sys_auditlog_list', null, '/system/auditlog/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_fwlog_look', '查看访问日志', 'sys_fwlog_list', null, '/system/fwlog/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_delete', '删除菜单', 'sys_menu_list', null, '/system/menu/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_deletemore', '批量删除菜单', 'sys_menu_list', null, '/system/menu/delete/more', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_list', '菜单管理', 'system_manager', null, '/system/menu/list', 1, true, 3, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_look', '查看菜单', 'sys_menu_list', null, '/system/menu/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_tree', '菜单树形结构', 'sys_menu_list', null, '/system/menu/tree', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_menu_update', '修改菜单', 'sys_menu_list', null, '/system/menu/update', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_delete', '删除部门', 'sys_org_list', null, '/system/org/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_deletemore', '批量删除部门', 'sys_org_list', null, '/system/org/delete/more', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_list', '部门管理', 'business_manager', null, '/system/org/list', 1, true, 1, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_look', '查看部门', 'sys_org_list', null, '/system/org/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_tree', '部门树形结构', 'sys_org_list', null, '/system/org/tree', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_org_update', '修改部门', 'sys_org_list', null, '/system/org/update', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_role_delete', '删除角色', 'sys_role_list', null, '/system/role/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_role_deletemore', '批量删除角色', 'sys_role_list', null, '/system/role/delete/more', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_role_list', '角色管理', 'system_manager', '', '/system/role/list', 1, true, 4, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_role_look', '查看角色', 'sys_role_list', null, '/system/role/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_role_update', '修改角色', 'sys_role_list', null, '/system/role/update', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_delete', '删除账号', 'sys_user_list', null, '/system/user/delete', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_deletemore', '批量删除账号', 'sys_user_list', null, '/system/user/delete/more', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_list', '账号管理', 'system_manager', null, '/system/user/list', 1, true, 3, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_lissys_export', '导出账号', 'sys_user_list', null, '/system/user/list/export', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_look', '查看账号', 'sys_user_list', null, '/system/user/look', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_user_update', '修改账号', 'sys_user_list', null, '/system/user/update', 0, true, null, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_dicdata_list', '字典管理', 'system_manager', null, '/system/dicdata/list', 1, true, 5, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_conf_list', '参数管理', 'system_manager', null, '/system/conf/toList', 1, true, 6, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('sys_staff_list', '人员管理', 'business_manager', null, '/system/staff/toList', 1, true, 2, null);
INSERT INTO sys_menu (id, name, pid, description, pageurl, type, state, seq, icon) VALUES ('ex_system_list', '外部系统管理', 'system_manager', null, '/system/exsystem/toList', 1, true, 10, null);

CREATE TABLE IF NOT EXISTS sys_org (id varchar(40) NOT NULL COMMENT '编号', name varchar(60) COMMENT '名称', comcode varchar(40) COMMENT '代码', pid varchar(40) COMMENT '上级部门ID', sysid varchar(40) COMMENT '子系统ID', type int COMMENT '0,组织机构 1.部门', leaf int COMMENT '叶子节点(0:树枝节点;1:叶子节点)', seq int COMMENT '排序号', description varchar(2000) COMMENT '描述', state Boolean DEFAULT true COMMENT '是否有效(否/是)', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_org (id, name, comcode, pid, sysid, type, leaf, seq, description, state) VALUES ('0', '虚拟管理组', 'adminorg', null, null, null, false, 1, '', true);
INSERT INTO sys_org (id, name, comcode, pid, sysid, type, leaf, seq, description, state) VALUES ('01', '测试部门', 'test', null, null, null, false, 1, '', true);
INSERT INTO sys_org (id, name, comcode, pid, sysid, type, leaf, seq, description, state) VALUES ('0101', '测试部门1', 'test1', null, null, null, false, 1, '', true);


CREATE TABLE IF NOT EXISTS sys_role (id varchar(40) NOT NULL COMMENT '角色ID', name varchar(60) COMMENT '角色名称', code varchar(255) COMMENT '权限编码', orgid varchar(40) NOT NULL COMMENT '所属部门ID', remark varchar(255) COMMENT '备注', state Boolean DEFAULT true COMMENT '是否有效(否/是)', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_role (id, name, code, orgid, remark, state) VALUES ('adminrole', '超级管理员', null, '0', '', true);
INSERT INTO sys_role (id, name, code, orgid, remark, state) VALUES ('adminrole_default', '默认管理员角色', null, '0', '', true);

CREATE TABLE IF NOT EXISTS sys_role_menu (id varchar(100) NOT NULL COMMENT '编号', roleId varchar(40) NOT NULL COMMENT '角色编号', menuId varchar(40) NOT NULL COMMENT '菜单编号', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('09bf2e03f127496cb8bb0b157ecb325a', 'adminrole', 'sys_org_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('23c6f39769da49049b574e445ec2a222', 'adminrole', 'system_manager');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('adminrole:sys_plugin_list', 'adminrole', 'sys_plugin_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('adminrole:sys_plugin_update', 'adminrole', 'sys_plugin_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('adminrole:sys_plugin_delete', 'adminrole', 'sys_plugin_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('adminrole:sys_plugin_enable', 'adminrole', 'sys_plugin_enable');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('adminrole:sys_plugin_mount', 'adminrole', 'sys_plugin_mount');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('24e38c16d029435882a89777fe8d7722', 'adminrole', 'sys_org_tree');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('24e38c16d029435882a89777fe8d7822', 'adminrole', 'sys_org_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('29c92c1e1b4a4c13b5c731b9997e0c82', 'adminrole', 'sys_menu_tree');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('29c92c1e1b4a4c13b5c731b9997e0c92', 'adminrole', 'sys_menu_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('2fa0a289b0a04d508bede70dcd76516c', 'adminrole', 'sys_user_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('45a071f489024a769173b9a9418e1312', 'adminrole', 'sys_auditlog_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('4c5ecbab923347d88ff41391eec84b2c', 'adminrole', 'sys_auditlog_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('6780735e60564141bb3ba88c1b6cf0bd', 'adminrole', 'sys_fwlog_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('69e596a905a049ec98759d8dd0ec22ca', 'adminrole', 'sys_role_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('709609415b60464e823ecbb8cde40ae7', 'adminrole', 'sys_role_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('7c1cc86c2f8a4037a61073935acaa6e6', 'adminrole', 'sys_role_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('966b72eb5aec46a6ae90731e35c260b6', 'adminrole', 'sys_org_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('a407a97128ae418ebb02401b7a36a200', 'adminrole', 'sys_fwlog_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('a46e434392ac4e169d41a2d454d04e09', 'adminrole', 'sys_menu_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('a56cbbbb5d80460aa906a7548ac0be52', 'adminrole', 'sys_menu_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('ae758ef55f374d28a034d1ed12c4c8b3', 'adminrole', 'sys_role_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('afcf3bb5bc5b4cc3bebf2c31772222c3', 'adminrole', 'sys_user_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('b981bf4d6c344f6d840ab4aac0b12e02', 'adminrole', 'sys_user_lissys_export');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('ba87ada9266f49849e83343f65a39d28', 'adminrole', 'sys_user_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('bb95749540704db6a8478992c7fc36cb', 'adminrole', 'sys_user_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('be1c18c1c91b4268b16f1e26258a137c', 'adminrole', 'sys_role_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('c1fce66d083443bc8726c515f8952653', 'adminrole', 'sys_user_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d65212b71f044973990ca37f770f20d2', 'adminrole', 'business_manager');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('de4a08807d274bd89e556e0ef907eff6', 'adminrole', 'sys_org_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('e0602d38c0ce483181a925d4bb321593', 'adminrole', 'sys_menu_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('e6f1fd90c4064c5396dcd95f865fbd98', 'adminrole', 'sys_menu_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('fb2021ec726b44709090e5df9dbe2e7a', 'adminrole', 'sys_org_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('fb2021ec726b44709090e5df9dbe2e71', 'adminrole', 'sys_dicdata_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('fb2021ec726b44709090e5df9dbe2e72', 'adminrole', 'sys_conf_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('fb2021ec726b44709090e5df9dbe2e73', 'adminrole', 'sys_staff_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('fb2021ec726b44709090e5df9dbe2e74', 'adminrole', 'ex_system_list');

INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d1', 'adminrole_default', 'business_manager');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d2', 'adminrole_default', 'system_manager');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d3', 'adminrole_default', 'sys_org_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d4', 'adminrole_default', 'sys_org_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d5', 'adminrole_default', 'sys_org_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d6', 'adminrole_default', 'sys_org_tree');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d7', 'adminrole_default', 'sys_org_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d8', 'adminrole_default', 'sys_org_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d9', 'adminrole_default', 'sys_user_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d10', 'adminrole_default', 'sys_user_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d11', 'adminrole_default', 'sys_user_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d12', 'adminrole_default', 'sys_user_lissys_export');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d13', 'adminrole_default', 'sys_user_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d14', 'adminrole_default', 'sys_user_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d15', 'adminrole_default', 'sys_role_list');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d16', 'adminrole_default', 'sys_role_look');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d17', 'adminrole_default', 'sys_role_update');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d18', 'adminrole_default', 'sys_role_delete');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d19', 'adminrole_default', 'sys_role_deletemore');
INSERT INTO sys_role_menu (id, roleId, menuId) VALUES ('d20', 'adminrole_default', 'sys_staff_list');

CREATE TABLE IF NOT EXISTS sys_staff (id varchar(40) NOT NULL COMMENT '编号', orgid varchar(40) COMMENT '部门编号', name varchar(30) COMMENT '姓名', workno varchar(40) COMMENT '工号', cardno varchar(40) COMMENT '身份证', age int COMMENT '年龄', sex varchar(2) DEFAULT '男' COMMENT '性别', phone varchar(16) COMMENT '电话号码', mobile varchar(16) COMMENT '手机号码', email varchar(60) COMMENT '邮箱', address varchar(255) COMMENT '地址', gradeId varchar(40) COMMENT '级别', eduName varchar(40) COMMENT '学历', fireName varchar(30) COMMENT '紧急联系人', firePhone varchar(30) COMMENT '紧急联系电话', remark varchar(2000) COMMENT '备注', weixinId varchar(200) COMMENT '微信Id', state Boolean DEFAULT true COMMENT '是/否/离职', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('admin', '0', 'admin', '0000', null, null, '男', null, null, null, null, '936db407-ae3-45a7-a657-b60580e2a77a', null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user1', '01', 'user1', '0001', null, null, '男', null, null, null, null, '936db407-ae3-45a7-a657-b60580e2a77a', null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user2', '01', 'user2', '0002', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user3', '01', 'user3', '0003', null, null, '女', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user4', '01', 'user4', '0004', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user5', '01', 'user5', '0005', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user6', '01', 'user6', '0006', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user7', '01', 'user7', '0007', null, null, '女', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user8', '01', 'user8', '0008', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user9', '01', 'user9', '0009', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user10', '01', 'user10', '0010', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user11', '01', 'user11', '0011', null, null, '男', null, null, null, null, null, null, null, null, null, null, true);
INSERT INTO sys_staff (id, orgid, name, workno, cardno, age, sex, phone, mobile, email, address, gradeId, eduName, fireName, firePhone, remark, weixinId, state) VALUES ('user12', '01', 'user12', '0012', null, null, '女', null, null, null, null, null, null, null, null, null, null, true);


CREATE TABLE IF NOT EXISTS sys_user (id varchar(40) NOT NULL COMMENT '编号', orgid varchar(40) COMMENT '部门编号', staffid varchar(40) COMMENT '人员编号', account varchar(40) COMMENT '账号', password varchar(40) COMMENT '密码',name varchar(30) COMMENT '姓名', workno varchar(40) COMMENT '工号',mobile varchar(16) COMMENT '手机号码', remark varchar(2000) COMMENT '备注', state Boolean DEFAULT true COMMENT '启用停用', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_user (id, orgid, staffid, account, password, name, workno, remark, state) VALUES ('admin', '0', 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', 'admin', null, true);

CREATE TABLE IF NOT EXISTS sys_user_role (id varchar(40) NOT NULL COMMENT '编号', userId varchar(40) NOT NULL COMMENT '账号ID', roleId varchar(40) NOT NULL COMMENT '角色编号', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO sys_user_role (id, userId, roleId) VALUES ('admin_adminrole', 'admin', 'adminrole');

CREATE TABLE IF NOT EXISTS ex_system (id varchar(40) NOT NULL, name varchar(60), pageurl varchar(3000), accountname varchar(40),passwordname varchar(40), charset varchar(20) DEFAULT 'UTF-8' COMMENT '编码格式',type int COMMENT '1.直接登录,2.跳转到登录页面', remark varchar(2000) COMMENT '备注',  state Boolean DEFAULT true COMMENT '是否有效', seq float, icon varchar(100), deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('1', '劳资管理系统', null, 1, true, 1, 'fa-archive');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('2', '接触网运行检修管理系统', null, 1, true, 2, 'fa-train');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('3', '变配电运行检修管理系统', null, 1, true, 3, 'fa-university');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('4', '电力运行检修管理系统', null, 1, true, 4, 'fa-flash');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('5', '给水运行检修管理系统', null, 1, true, 5, 'fa-filter');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('6', '机械动力设备管理系统', null, 1, true, 6, 'fa-wrench');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('7', '安全风险管理系统', null, 1, true, 7, 'fa-thumbs-o-up');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('8', '轨道车管理系统', null, 1, true, 8, 'fa-train');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('9', '异常信息管理系统', null, 1, true, 9, 'fa-warning');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('10', '应急指挥系统', null, 1, true, 10, 'fa-flag-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('11', '固定资产管理系统', null, 1, true, 11, 'fa-desktop');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('12', '标准规章执行管理系统', null, 1, true, 12, 'fa-book');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('13', '工具及劳保用品管理系统', null, 1, true, 13, 'fa-gavel');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('14', '水电费管理系统', null, 1, true, 14, 'fa-rmb');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('15', '对讲机管理系统', null, 1, true, 15, 'fa-phone');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('16', '资质及培训教育管理系统', null, 1, true, 16, 'fa-file-powerpoint-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('17', '科研活动管理系统', null, 1, true, 17, 'fa-connectdevelop');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('18', '党建思想工作管理系统', null, 1, true, 18, 'fa-bookmark-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('19', '办公及后勤资产管理系统', null, 1, true, 19, 'fa-file-word-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('20', '合同管理系统', null, 1, true, 20, 'fa-file-text-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('21', '合理化建议管理系统', null, 1, true, 21, 'fa-send-o');
INSERT INTO ex_system (id, name, pageurl, type, state, seq, icon) VALUES ('22', '费用报销管理系统', null, 1, true, 22, 'fa-money');
INSERT INTO ex_system (id, name, pageurl, accountname, passwordname, type, charset, state, seq, icon) VALUES ('23', 'OA系统', 'http://192.168.0.137:8080/logincheck.php','USERNAME','PASSWORD', 1, 'GB2312', true, 23, 'fa-envelope');
INSERT INTO ex_system (id, name, pageurl, accountname, passwordname, type, state, seq, icon) VALUES ('24', 'JIRA', 'http://192.168.0.233:8020/login.jsp?os_destination=/secure/Dashboard.jspa','os_username','os_password', 1, true, 24, 'fa-send-o');

CREATE TABLE IF NOT EXISTS ex_user (id varchar(40) NOT NULL, exsystemid varchar(40) NOT NULL, userid varchar(40) NOT NULL, account varchar(40) COMMENT '账号', password varchar(40) COMMENT '密码', state Boolean DEFAULT true COMMENT '是否有效', deleted Boolean DEFAULT false COMMENT '删除状态', PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO ex_user (id, exsystemid, userid, account, password, state) VALUES ('1', '23', 'admin', '蒋凯', 'ssjk819oa', true);
INSERT INTO ex_user (id, exsystemid, userid, account, password, state) VALUES ('2', '24', 'admin', 'jiangk', 'ssjk819jira', true);

-- ALTER TABLE sys_role_menu ADD CONSTRAINT fk_sys_role_menu_menuId_sys_menu_id FOREIGN KEY (menuId) REFERENCES sys_menu (id) ;
-- ALTER TABLE sys_role_menu ADD CONSTRAINT fk_sys_role_menu_roleId_sys_role_id FOREIGN KEY (roleId) REFERENCES sys_role (id);
-- ALTER TABLE sys_user_org ADD CONSTRAINT fk_sys_user_org_orgId_sys_org_id FOREIGN KEY (orgId) REFERENCES sys_org (id) ;
-- ALTER TABLE sys_user_org ADD CONSTRAINT fk_sys_user_org_userId_sys_user_id FOREIGN KEY (userId) REFERENCES sys_user (id);
-- ALTER TABLE sys_user_role ADD CONSTRAINT fk_sys_user_role_roleId_sys_role_id FOREIGN KEY (roleId) REFERENCES sys_role (id) ;
-- ALTER TABLE sys_user_role ADD CONSTRAINT fk_sys_user_role_userId_sys_user_id FOREIGN KEY (userId) REFERENCES sys_user (id);

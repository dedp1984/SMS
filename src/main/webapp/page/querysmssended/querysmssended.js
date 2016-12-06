Ext.application({
	name : '账户管理',
	launch : function() {
		
		var editAction;
		var gridStore = Ext.create('Ext.data.Store', {
			autoLoad : false,
			pageSize:20,
			fields : [ 'id', 
			           'srcchnl',
			           'detailid' ,
			           'content',
			           'tel',
			           'procstatus',
			           'sendtaskid',
			           'sendtime'],
			proxy : {
				type : 'ajax',
				actionMethods:{
					create: 'POST', 
					read: 'POST', 
					update: 'POST', 
					destroy: 'POST'
				},
				reader: {  
                    type:'json',
                    root:'items',
                    totalProperty: 'totalsize'
				},
				url : '../../action/pujjr/querySmsSendedList'
			}
		});
		var queryGrid = Ext.create('Ext.grid.Panel', {
			anchor:'100% 70%',
			store : gridStore,
			flex:2,
			columns : [ {
				header : '来源渠道',
				dataIndex : 'srcchnl',
				width:70
			},  {
				header : '短信内容',
				dataIndex : 'content',
				flex:1,
				renderer: function(value, meta, record) {
					meta.style = 'white-space:normal;word-break:break-all;';
			        return value;
			    }
			}, {
				header : '接收号码',
				dataIndex : 'tel',
				width:100
			},{
				header : '处理状态',
				dataIndex : 'procstatus',
				width:100
			},{
				header : '发送任务号',
				dataIndex : 'sendtaskid',
				width:100
			},{
				header : '发送时间',
				dataIndex : 'sendtime',
				width:140
			},{
				header : '编辑用户',
				xtype : 'actioncolumn',
				width:100,
				align:'center',
				hidden:true,
				items : [ {
					icon:'../../images/detail.gif',
					handler : function(grid, rowIndex, colIndex, item, e) {
						editForm.getForm().reset();
						editForm.getForm().loadRecord(grid.getStore().getAt(rowIndex));
						var checkgroupItems=editForm.getForm().findField('busifeatures').items.items;
						var busiFeature=grid.getStore().getAt(rowIndex).get('busiFeature');
						for(var i=0;i<checkgroupItems.length;i++){
							var item=checkgroupItems[i];
							item.setValue(false);
							for(var j=0;j<busiFeature.length;j++){
								var feature=busiFeature[j];
								if(item.inputValue==feature.value){
									item.setValue(true);
									break;
								}
								
							}
						}
						/*
						checkgroupItems.each(function(f){
							busiFeature.each(function(b){
								if(b.value==f.inputValue){
									f.checked=true;
								}
							})
						})*/
						dsFrom.load({
							callback:function(){
								var roleList=grid.getStore().getAt(rowIndex).get('roles');
								var roleArray=new Array();
								for(i=0;i<roleList.length;i++){
									var roleid=roleList[i].roleid;
									roleArray.push(roleid);
								}
								editForm.getForm().findField('roleItemSelector').setValue(roleArray);
							}
						});
						editForm.getForm().findField('accountid').setReadOnly(true);
						editAction=true;
						queryPanel.hide();
						editPanel.anchor='100% 100%';
						editPanel.updateLayout();
						editPanel.show();
					}
				} ]
			},{
				header : '删除用户',
				xtype : 'actioncolumn',
				width:100,
				hidden:true,
				align:'center',
				items : [ {
					icon:'../../images/del.gif',
					//iconCls:'fa fa-times',
					handler : function(grid, rowIndex, colIndex, item, e) {
						var currentAccountId=grid.getStore().getAt(rowIndex).get('accountid');
						var currentAccountName=grid.getStore().getAt(rowIndex).get('accountname');
						Ext.Msg.show({
							msg:'确定删除【'+currentAccountName+'】?', 
							title:'信息提示', 
							buttons:Ext.Msg.OKCANCEL,
							fn:function(btn, text){
								if (btn == 'ok'){
							    	var request=Ext.Ajax.request({
										params: {
									        accountid: currentAccountId
									    },
										url:'../../action/account/deleteAccount',
										success:function(response,options){
											if(Ext.JSON.decode(response.responseText).success==true){
												Ext.Msg.alert('信息提示', '删除用户成功');
												gridStore.load();
											}else{
												alert('删除用户失败:'+Ext.JSON.decode(response.responseText).errors.errmsg);
											}
											
										}
									});
							    }
							}
						})
					}
				} ]
			}],
			
			dockedItems : [{
				xtype:'toolbar',
				dock:'top',
				hidden:true,
				items:['->',{
					glyph:'xf067@FontAwesome',
					text:'增加用户',
					handler:function(){
						queryPanel.hide();
						editPanel.anchor='100% 100%';
						editPanel.updateLayout();
						editPanel.show();
						editForm.getForm().reset();
						dsFrom.load({
							callback:function(){
								editForm.getForm().findField('roleItemSelector').setValue([]);
							}
						});
						editForm.getForm().findField('accountid').setReadOnly(false);
						editAction=false;
					}
				}]
			}, {
				xtype : 'pagingtoolbar',
				store : gridStore, // GridPanel使用相同的数据源
				dock : 'bottom',
				displayInfo : true
			} ]
		});
		
		
		var dsFrom = Ext.create('Ext.data.ArrayStore', {
	        fields: ['roleid','rolename'],
	        proxy: {
	            type: 'ajax',
	            url: '../../ajax/role/queryRoleListByRoleName',
	            reader: 'json'
	        },
	        autoLoad: false,
	        sortInfo: {
	            field: 'roleid',
	            direction: 'ASC'
	        }
	    });
		
		var editForm =Ext.create('Ext.form.Panel',{
			bodyBorder:false,
			paramsAsHash: true,
			layout : 'form',
			items:[{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.4,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						width:400,
						fieldLabel : '用户ID',
						name:'accountid',
						allowBlank:false
						}]
				},{
					columnWidth:0.4,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						width:400,
						fieldLabel : '机构ID',
						name:'branchid',
						allowBlank:false,
						hidden:true
						
						}]
				},{
					columnWidth:0.4,  //该列占用的宽度，标识为50％
					layout:{
						type:'hbox',
						align:'middle'
					},
					border:false,
					items:[{
						xtype:'textfield',
						width:400,
						fieldLabel : '姓名',
						name:'accountname',
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.4,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '所属机构',
						width:400,
						name:'branch.branchname',
						allowBlank:false,
						readOnly:true
						},{
							xtype:'button',
							//icon:'../../images/search-trigger.gif',
							glyph:'xf002@FontAwesome',
							handler:function(button,e){
								var treeStore= Ext.create('Ext.data.TreeStore', {
								    root: {
								    	text:'根节点',
								        expanded: true
								    },
								    nodeParam: 'id',
								    defaultRootId:0,
								    fields:[{name:'id',type:'string'},
								            {name:'text',type:'string'},
								            {name:'leaf',type:'boolean'},
								            {name:'parentid',type:'string'}
								            ],
								     proxy: {
												type:'ajax',
												method:'post',
												url:'../../action/branch/queryBranchTreeByAccountId'
											}
								});
								var treePanel = Ext.create('Ext.tree.Panel', {
									bodyPadding: '20 20 0',
									store:treeStore,
									rootVisible: false,
								    aoutLoad:true,
									listeners:{
										itemclick:function(view,record,item,index,e,opt){
											editForm.getForm().findField('branchid').setValue(record.get('id'));
											editForm.getForm().findField('branch.branchname').setValue(record.get('text'));
											win.close();
										}
									}
								});
								var win = Ext.create('Ext.window.Window', {
								    title: '选择区域',
								    collapsible: true,
								    animCollapse: true,
								    maximizable: true,
								    closeAction:'destory',
								    width: 300,
								    height: 400,
								    minWidth: 300,
								    minHeight: 200,
								    layout: 'fit',
								    items: [treePanel]
								});
								win.showAt(e.getXY());
							}
						
							}]
				},{
					columnWidth:0.4,
					layout:'hbox',
					border:false,
					items:[{
						xtype:'combobox',
						width:400,
						fieldLabel : '用户类型',
						name:'property',
						displayField:'name',  
				        valueField:'value',
				        forceSelection: true,
				        editable:false,
				        allowBlank:true,
				        blankText:'请选择用户类型',
			            emptyText:'请选择用户类型',
				        store:{  
				            type:'array',  
				            fields:['value','name'],  
				            data:[  
				                ['1','行领导'],  
				                ['2','部门领导'],
				                ['3','客户经理'],
				                ['4','系统管理员'],
				                ['5','虚拟客户经理'],
				                ['6','高级用户']
				            ] 
				        }
					}]
					
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.4,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
				        xtype: 'checkboxgroup',
				        name:'busifeatures',
				        fieldLabel: '业务范围',
				        allowBlank:false,
				        // Arrange checkboxes into two columns, distributed vertically
				        columns: 2,
				        vertical: true,
				        items: [
				            { boxLabel: '对公业务', width:100,name: 'busifeature', inputValue: '1'},
				            { boxLabel: '个人业务', width:100,name: 'busifeature', inputValue: '2'}
				        ]
				    }]
				}]
			},{
				xtype:'panel',
				layout:'column',
				border:false,
				items:[{
					xtype : 'itemselector',
					columnWidth:0.8,
					name:'rolelist',
		            id: 'roleItemSelector',
		            fieldLabel: '选择角色',
		            hideLabel : false,
		            imagePath: '../ux/images/',
		            store:dsFrom,
		            displayField: 'rolename',
		            valueField: 'roleid',
		            allowBlank: false,
		            msgTarget: 'side',
		            fromTitle: '可选角色',
		            toTitle: '已选角色'
					}]
			}],
			dockedItems : [{
				xtype:'toolbar',
				dock:'top',
				items:['->',{
					glyph:'xf0c7@FontAwesome',
					text:'保存',
					handler:function(){
						if(editForm.getForm().isValid()){
							editForm.getForm().submit({
								url:editAction==false?'../../action/account/addAccount':'../../action/account/modifyAccount',
								success:function(form,action){
									Ext.Msg.alert('信息提示', editAction==false?'增加用户成功':'修改用户成功');
									editPanel.hide();
									queryPanel.anchor='100% 100%';
									queryPanel.updateLayout();
									queryPanel.show();
									gridStore.load();
								},
								failure:function(form,action){
									Ext.Msg.alert('信息提示','增加用户失败');
								}
							})
						}
						
					}
				},{
					glyph:'xf0e2@FontAwesome',
					text:'返回查询',
					handler:function(){
						editPanel.hide();
						queryPanel.anchor='100% 100%';
						queryPanel.updateLayout();
						queryPanel.show();
					}
				}]
			}]
		});
		
		var queryForm=Ext.create('Ext.form.Panel',{
			border:1,
			bodyPadding:'20 20 0',
			anchor:'100% 30%',
			paramsAsHash: true,
			layout : 'form',
			items : [{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '短信内容',
						name:'content',
						width:300
						}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:{
						type:'hbox'
					},
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '接收号码',
						name:'tel',
						width:300
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'datefield',
						fieldLabel : '开始日期',
						name:'startdate',
						format:'Y-m-d',
						width:300
						}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:{
						type:'hbox'
					},
					border:false,
					items:[{
						xtype:'datefield',
						fieldLabel : '结束日期',
						name:'enddate',
						format:'Y-m-d',
						width:300
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'combobox',
						width:300,
						fieldLabel : '发送结果',
						name:'status',
						displayField:'name',  
				        valueField:'value',
				        editable:false,
				        allowBlank:true,
				        value:'发送失败', 
				        blankText:'请选择发送结果',
			            emptyText:'请选择发送结果',
				        store:{  
				            type:'array',  
				            fields:['value','name'],  
				            data:[  
				                ['','全部'],
				                ['发送成功','发送成功'],  
				                ['发送失败','发送失败'],
				                ['已发送','已发送'],
				                ['待处理','待处理']
				            ] 
				        }
					}]
				}]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'查询',
				glyph:'xf002@FontAwesome',
				handler:function(){
					if(queryForm.getForm().isValid()){
						var content=queryForm.getForm().findField('content').getValue();
						var tel=queryForm.getForm().findField('tel').getValue();
						var startdate= Ext.util.Format.date(queryForm.getForm().findField('startdate').getValue(),'Y-m-d');
						var enddate= Ext.util.Format.date(queryForm.getForm().findField('enddate').getValue(),'Y-m-d');
						var status=queryForm.getForm().findField('status').getValue();
						gridStore.getProxy().extraParams = {
							'content' : content,
							'tel':tel,
							'startdate':startdate,
							'enddate':enddate,
							'status':status
						};
						gridStore.currentPage=1;
						gridStore.load();
					}
				}
			},{
				text:'重置',
				glyph:'xf021@FontAwesome',
				handler:function(){
					queryForm.getForm().reset();
				}
			}]
		});
		//角色信息列表显示
		var queryPanel = Ext.create('Ext.panel.Panel', {
			name:'queryPanel',
			title : '用户信息查询',
			anchor:'100% 100%',
			layout : {
				type:'anchor'
			},
			items : [queryForm,queryGrid]
		});
		
		
	    
		var editPanel =Ext.create('Ext.panel.Panel',{
			name:'editPanel',
			title:'已发送短信查询',
			anchor:'100% 100%',
			bodyBorder:false,
			hidden:true,
			layout:'fit',
			items:[editForm]
		});
		
		Ext.create('Ext.container.Viewport', {
			layout :'anchor',
			items : [ queryPanel, editPanel ],
			renderTo : Ext.getBody()
			
		})
	}
})
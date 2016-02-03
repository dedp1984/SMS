
Ext.application({
	name : '短信模板管理',
	launch : function() {
		var editAction;
		var queryForm=Ext.create('Ext.form.Panel',{
			border:1,
			bodyPadding:'20 20 0',
			anchor:'100% 20%',
			paramsAsHash: true,
			layout : 'column',
			items : [ {
				columnWidth:0.5,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'textfield',
					fieldLabel : '模板名称',
					name:'name'
					}]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'查询',
				glyph:'xf002@FontAwesome',
				handler:function(){
					if(queryForm.getForm().isValid()){
						var name = queryForm.getForm().findField('name').getValue();
						gridStore.getProxy().extraParams = {
							'name' : name
						};
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
		
		
		var gridStore = Ext.create('Ext.data.Store', {
			autoLoad : false,
			fields : [
			          {name:'id',type:'string'},
			          {name:'name',type:'string'},
			          {name:'content',type:'string'},
			          {name:'startcolnum',type:'number'},
			          {name:'telvarname',type:'string'},
			          {name:'attachname',type:'string'},
			          {name:'attachpath',type:'string'},
			          {name:'modifyid',type:'string'},
			          {name:'modifydate',type:'date'},'detail'],
			pageSize:15,
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
				url : '../../action/pujjr/queryTemplateList'
			}
		});
		
		Ext.define('bindForm',{
			extend : 'Ext.form.Panel',
			border:false,
			layout:'column',
			name:'bindForm',
			items:[{
				columnWidth:0.15,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'numberfield',
					fieldLabel:'序号',
					width:100,
					labelWidth:30,
					name:'seq',
					allowBlank:false,
					value:1,
					minValue: 1,
					maxValue:100
					}]
			},{
				columnWidth:0.25,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'textfield',
					fieldLabel:'变量备注',
					width:200,
					labelWidth:70,
					name:'comment',
					allowBlank:false
					}]
			},{
				columnWidth:0.25,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'textfield',
					fieldLabel:'变量名称',
					width:200,
					labelWidth:70,
					name:'varname',
					allowBlank:false
					}]
			},{
				columnWidth:0.15,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'textfield',
					fieldLabel:'excel列',
					labelWidth:70,
					name:'colnum',
					allowBlank:false
					}]
			},{
				columnWidth:0.2,  //该列占用的宽度，标识为50％
				layout:{
					type:'hbox',
					pack:'center'
				},
				border:false,
				items:[{
					xtype:'button',
					text:'增加',
					handler:function(){
						var length=bindPanel.items.length;
						var bindForm=new Ext.create('bindForm');
						bindForm.id='bindForm'+length;
						bindPanel.insert(length,bindForm);
						bindPanel.doLayout();
					}
					},{
						xtype:'button',
						text:'删除',
						handler:function(button){
							var parent=button.findParentByType('form');
							bindPanel.remove(parent);
							if(bindPanel.items.length==0){
								var bindForm=new Ext.create('bindForm');
								bindPanel.insert(length,bindForm);
							}
							bindPanel.doLayout();
						}
						}]
			}]
		});

		
		var bindPanel=Ext.create('Ext.form.FieldSet',{
			title:'模板详细信息',
			border:true
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
					columnWidth:1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					hidden:true,
					items:[{
						xtype:'textfield',
						fieldLabel : '模板编号',
						name:'id',
						width:300
					}]
				},{
					columnWidth:1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '模板名称',
						name:'name',
						width:300,
				        allowBlank:false,
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
						xtype:'numberfield',
						fieldLabel : '明细起始行',
						width:300,
						name:'startColNum',
						allowBlank:false
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
						xtype:'textfield',
						fieldLabel : '手机变量名称',
						width:300,
						name:'telVarName',
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textarea',
						fieldLabel : '短信内容',
						width:800,
						name:'content',
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				id:'uppanel',
				items:[{
					columnWidth:1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'filefield',
						fieldLabel : '模板文件',
						msgTarget: 'side',
						width:500,
						name:'attachFile',
				        buttonText: '选择文件...',
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				id:'attachpanel',
				items:[{
					columnWidth:0.1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						width:100,
						xtype:'label',
						text:'模板附件：'
						}]
				},{
					columnWidth:0.15,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						id:'attachment',
						columnWidth:0.1,
						xtype:'label',
						name:'attachment',
						html:'&nbsp;&nbsp;&nbsp;&nbsp;<a href="../../action/download?path=template/个人定期存款日均模板.xls">test</a>'
					}]
				},{
					columnWidth:0.1,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						columnWidth:0.3,
						xtype:'button',
						text:'重新上传',
						listeners:{
							click:function(){
								Ext.getCmp('uppanel').setDisabled(false);
								Ext.getCmp('uppanel').setVisible(true);
								editForm.getForm().findField('attachFile').allowBlank=true;
							}
						}
					}]
				}]
			},bindPanel],
			dockedItems : [{
				xtype:'toolbar',
				dock:'top',
				items:['->',{
					glyph:'xf0c7@FontAwesome', 
					text:'保存',
					handler:function(){
						if(editForm.getForm().isValid()){
							var bindLen=bindPanel.items.length;
							var bindlist='';
							var detailStr='';
							for(var i=0;i<bindLen;i++){
								var bindForm=bindPanel.items.getAt(i).getForm();
								var seq=bindForm.findField('seq').getValue();
								var comment=bindForm.findField('comment').getValue();
								var varname=bindForm.findField('varname').getValue();
								var colnum=bindForm.findField('colnum').getValue();
								detailStr+=seq+','+comment+','+varname+','+colnum+'#';
							}
							editForm.getForm().submit({
								url:editAction==true?'../../action/pujjr/updateTemplate':'../../action/pujjr/addTemplate',
								params:{
									detailStr:detailStr
								},
								success:function(form,action){
									Ext.Msg.alert('信息提示', '操作成功');
									editPanel.hide();
									queryPanel.anchor='100% 100%';
									queryPanel.updateLayout();
									queryPanel.show();
									gridStore.load();
								},
								failure:function(form,action){
									Ext.Msg.alert('信息提示',action.result.errors.errmsg);
								}
							})
						}
						
					}
				},{
					glyph:'xf0e2@FontAwesome',
					text:'返回',
					handler:function(){
						editPanel.hide();
						queryPanel.anchor='100% 100%';
						queryPanel.updateLayout();
						queryPanel.show();
					}
				}]
			}]
		});
		var queryGrid = Ext.create('Ext.grid.Panel', {
			anchor:'100% 80%',
			store : gridStore,
			flex:2,
			columns : [ {
				header : '模板名称',
				dataIndex : 'name',
				width:150
			}, {
				header : '短信内容',
				flex:1,
				dataIndex : 'content'
			}, {
				header : '明细起始行',
				dataIndex : 'startcolnum',
				xtype: 'numbercolumn',
				format:'0',
				width:150
			},{
				header : '手机号码变量',
				dataIndex : 'telvarname',
				width:150
			},{
				header : '查看详细',
				xtype : 'actioncolumn',
				width:100,
				align:'center',
				items : [ {
					icon:'../../images/detail.gif',
					handler : function(grid, rowIndex, colIndex, item, e) {
						editForm.getForm().reset();
						editForm.getForm().loadRecord(grid.getStore().getAt(rowIndex));
						var name=grid.getStore().getAt(rowIndex).get('name');
						var content=grid.getStore().getAt(rowIndex).get('content');
						var startcolnum=grid.getStore().getAt(rowIndex).get('startcolnum');
						var telvarname=grid.getStore().getAt(rowIndex).get('telvarname');
						editForm.getForm().findField('name').setValue(name);
						editForm.getForm().findField('content').setValue(content);
						editForm.getForm().findField('startColNum').setValue(startcolnum);
						editForm.getForm().findField('telVarName').setValue(telvarname);
						var attachname=grid.getStore().getAt(rowIndex).get('attachname');
						var attachpath=grid.getStore().getAt(rowIndex).get('attachpath'); 
						Ext.getCmp('attachpanel').setVisible(true);
						editForm.getForm().findField('attachFile').allowBlank=true;
						Ext.getCmp('uppanel').setDisabled(true);
						Ext.getCmp('uppanel').setVisible(false);
						Ext.getCmp('attachment').setText('<a href="../../action/download?path='+attachpath+'">'+attachname+'</a>',false);
						bindPanel.removeAll();
						var detail=grid.getStore().getAt(rowIndex).get('detail');
						for(var i=0;i<detail.length;i++){
							var bind=detail[i];
							var bindForm=new Ext.create('bindForm');
							var seq=bind.seq;
							var comment=bind.comment;
							var varname=bind.varname;
							var colnum=bind.colnum;
				
							bindForm.getForm().findField('seq').setValue(seq);
							bindForm.getForm().findField('comment').setValue(comment);
							bindForm.getForm().findField('varname').setValue(varname);
							bindForm.getForm().findField('colnum').setValue(colnum);
							bindForm.id='bindForm'+i;
							bindPanel.insert(i,bindForm);
						}
						editAction=true;
						queryPanel.hide();
						editPanel.anchor='100% 100%';
						editPanel.updateLayout();
						editPanel.show();
					}
				} ]
			},{
				header : '删除',
				xtype : 'actioncolumn',
				width:100,
				align:'center',
				items : [ {
					icon:'../../images/del3.gif',
					handler : function(grid, rowIndex, colIndex, item, e) {
					var id=grid.getStore().getAt(rowIndex).get('id');
					var name=grid.getStore().getAt(rowIndex).get('name');
					Ext.Msg.show({
						msg:'确定删除【'+name+'】?', 
						title:'信息提示', 
						buttons:Ext.Msg.OKCANCEL,
						fn:function(btn, text){
							if (btn == 'ok'){
						    	var request=Ext.Ajax.request({
									params: {
								        id: id
								    },
									url:'../../action/pujjr/deleteTemplate',
									success:function(response,options){
										Ext.Msg.alert('信息提示', '删除成功');
										gridStore.load();
									}
								});
						    }
						}
					})
					}
				} ]
			}],
			
			dockedItems : [ {
				xtype:'toolbar',
				dock:'top',
				items:['->',{
					glyph:'xf067@FontAwesome',
					text:'增加短信模板',
					handler:function(){
						queryPanel.hide();
						editPanel.anchor='100% 100%';
						editPanel.updateLayout();
						editPanel.show();
						editForm.getForm().reset();
						editForm.getForm().findField('id').setDisabled(false);
						Ext.getCmp('attachpanel').setVisible(false);
						editForm.getForm().findField('attachFile').allowBlank=false;
						Ext.getCmp('uppanel').setDisabled(false);
						Ext.getCmp('uppanel').setVisible(true);
						bindPanel.removeAll();
						var length=bindPanel.items.length;
						var bindForm=new Ext.create('bindForm');
						bindForm.id='bindForm'+length;
						bindPanel.insert(length,bindForm);
						bindPanel.doLayout();
						editAction=false;
					}
				}]
			},{
				xtype : 'pagingtoolbar',
				store : gridStore, // GridPanel使用相同的数据源
				dock : 'bottom',
				displayInfo : true
			} ]
		});
		var queryPanel=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			title:'短信模板管理',
			anchor:'100% 100%',
			items:[queryForm,queryGrid]
		});
		var editPanel =Ext.create('Ext.panel.Panel',{
			name:'editPanel',
			title:'详细信息',
			anchor:'100% 100%',
			bodyBorder:false,
			hidden:true,
			layout:'fit',
			items:[editForm]
		});
		Ext.create('Ext.container.Viewport', {
			layout :'anchor',
			items : [queryPanel,editPanel],
			renderTo : Ext.getBody()
			
		})
	}
})
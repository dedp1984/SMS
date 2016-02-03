var confirmtaskid;
Ext.application({
	name : '批量短信任务管理',
	launch : function() {
		var editAction;
		var queryForm=Ext.create('Ext.form.Panel',{
			border:1,
			bodyPadding:'20 20 0',
			anchor:'100% 15%',
			paramsAsHash: true,
			layout : 'column',
			items : [ {
				columnWidth:0.3,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'textfield',
					fieldLabel : '任务名称',
					name:'taskname'
					}]
			},{
				columnWidth:0.3,  //该列占用的宽度，标识为50％
				layout:'hbox',
				border:false,
				items:[{
					xtype:'datefield',
					fieldLabel : '创建日期',
					name:'createdate',
					format:'Y-m-d'
					}]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'查询',
				glyph:'xf002@FontAwesome',
				handler:function(){
					if(queryForm.getForm().isValid()){
						var taskname = queryForm.getForm().findField('taskname').getValue();
						var createdate = Ext.util.Format.date(queryForm.getForm().findField('createdate').getValue(),'Y-m-d');
						gridStore.getProxy().extraParams = {
							'taskname' : taskname,
							'createdate':createdate
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
		
		
		var gridStore = Ext.create('Ext.data.Store', {
			autoLoad : false,
			pageSize:20,
			fields : ['taskid','taskname','tasktype','tplid','content','filename','totalcnt','istimertask','sendtime','procstatus','createid','createdate'],
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
				url : '../../action/pujjr/queryTaskList'
			}
		});
		var taskDtlStore = Ext.create('Ext.data.Store', {
			autoLoad : false,
			pageSize:20,
			fields : ['content','tel','procstatus','taskid','id'],
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
				url : '../../action/pujjr/queryTaskDtlList'
			}
		});
		var prevWindow=Ext.create('Ext.window.Window', {
		    title: '短息明细',
		    height: 500,
		    width: 800,
		    layout: 'fit',
		    closeAction: "hide",
		    modal:true,
		    items: [{
				xtype:'grid',
				store:taskDtlStore,
				columns:[{
					header:'短信内容',
					dataIndex:'content',
					flex:1,
					renderer: function(value, meta, record) {
						meta.style = 'white-space:normal;word-break:break-all;';
				        return value;
				    }
				},{
					header:'接收号码',
					dataIndex:'tel',
					width:150
				},{
					header:'处理状态',
					dataIndex:'procstatus',
					width:150
				}],
				dockedItems : [ {
					xtype:'toolbar',
					id:'confirm',
					dock:'top',
					items:['->',{
						glyph:'xf067@FontAwesome',
						text:'确认发送',
						handler:function(){
							Ext.Msg.wait('正在保存数据,请稍候','提示');
							Ext.Ajax.request({
							    url: '../../action/pujjr/confirmBatchSendMessageTask',
							    params: {
							        'taskid':confirmtaskid
							    },
							    success: function(response){
							    	Ext.Msg.hide();
							        var data = Ext.JSON.decode(response.responseText);
							        if(data.success==false){
							        	return;
							        }else{
							        	prevWindow.close();
							        	Ext.Msg.alert('信息提示','交易成功');
							        	editPanel.hide();
										queryPanel.anchor='100% 100%';
										queryPanel.updateLayout();
										queryPanel.show();
										gridStore.load();
							        }
							        	
							    }
							});
						}
					}]
				},{
					xtype : 'pagingtoolbar',
					store : taskDtlStore, // GridPanel使用相同的数据源
					dock : 'bottom',
					displayInfo : true
				} ]
			}    	
		    ]
		});
		var editForm =Ext.create('Ext.form.Panel',{
			bodyBorder:false,
			paramsAsHash: true,
			bodyPadding:'0 0 0 10 ',
			anchor:'100% 100%',
			layout : 'form',
			items:[{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '任务名称',
						name:'taskname',
						width:500,
						labelWidth:100,
						allowBlank:false
					}]
				}]
			},{
				xtype:'panel',
				layout:'column',
				border:false,
				items:[{
					columnWidth:0.8,
					layout:'hbox',
					border:false,
					items:[{
						width:500,
						xtype:'combobox',
						fieldLabel : '选择短信模板',
						name:'tplid',
						displayField:'name',  
				        valueField:'id',
				        forceSelection: true,
				        editable:false,
				        allowBlank:false,
				        blankText:'请选择短信模板',
			            emptyText:'请选择短信类型',
				        store:{  
				            type:'array',  
				            fields:["id","name"],
							proxy:{
								type:'ajax',
								url:'../../action/pujjr/queryTemplateList',
								actionMethods:{
									create: 'POST', 
									read: 'POST', 
									update: 'POST', 
									destroy: 'POST'
								},
								reader: {  
					                    type:'json',
					                    root:'items'
					            }
							}
				        },
				        listeners:{
				        	select:function(combo,records,opts){
					        	editForm.getForm().findField("content").setValue(records[0].raw.content);
					        	var attachpath=records[0].raw.attachpath;
					        	var attachname=records[0].raw.attachname;
					        	Ext.getCmp('attachment').setText('&nbsp;&nbsp;&nbsp;&nbsp;<a href="../../action/download?path='+attachpath+'">'+attachname+'下载'+'</a>',false);
				        	}
				        }
					},{
						id:'attachment',
						columnWidth:0.1,
						xtype:'label',
						name:'attachment'
					}]
					
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textarea',
						fieldLabel : '短信内容',
						name:'content',
						width:500,
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
						xtype:'checkbox',
						fieldLabel : '是否定时发送',
						width:500,
						name:'istimertask',
						boxLabel:'是',
						allowBlank:false,
						handler:function(checkbox,checked){
							if(checked){
								editForm.getForm().findField("senddate").setDisabled(false);
								editForm.getForm().findField("senddate").allowBlank=true;
								editForm.getForm().findField("sendtime").setDisabled(false);
								editForm.getForm().findField("sendtime").allowBlank=true;
							}else{
								editForm.getForm().findField("senddate").setDisabled(true);
								editForm.getForm().findField("senddate").allowBlank=false;
								editForm.getForm().findField("sendtime").setDisabled(true);
								editForm.getForm().findField("sendtime").allowBlank=false;
							}
						}
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.25,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'datefield',
						fieldLabel : '发送日期',
						labelWidth:100,
						disabled:true,
						width:250,
						name:'senddate',
						format:'Y-m-d'
						}]
				},{
					columnWidth:0.25,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'timefield',
						fieldLabel : '发送时间',
						width:220,
						disabled:true,
						labelWidth:70,
						name:'sendtime',
						format:'H:i'
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype: 'filefield',
				        name: 'file',
				        fieldLabel: '数据文件',
				        msgTarget: 'side',
				        labelWidth:100,
				        width:500,
				        allowBlank: false,
				        buttonText: '选择文件...'
						}]
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
								url:editAction==true?'../../action/pujjr/createBatchSendMessageTask':'../../action/pujjr/createBatchSendMessageTask',
								waitMsg:'正在保存数据，请稍候。',
								waitTitle:'提示',		
								success:function(form,action){
									confirmtaskid=action.result.items.taskid;
									if(editForm.getForm().findField('istimertask').getValue()==false){
										Ext.getCmp('confirm').setVisible(true);
										prevWindow.show();
										taskDtlStore.getProxy().extraParams = {
											'taskid' : confirmtaskid
										};
										taskDtlStore.currentPage=1;
										taskDtlStore.load();
									}else{
										editPanel.hide();
										queryPanel.anchor='100% 100%';
										queryPanel.updateLayout();
										queryPanel.show();
										gridStore.load();
									}
									
								},
								failure:function(form,action){
									Ext.Msg.hide();
									Ext.Msg.alert('交易失败',action.result.errors.errmsg);
								}
							});
							
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
			anchor:'100% 85%',
			store : gridStore,
			columns : [{
				header : '任务名称',
				dataIndex : 'taskname',
				width:120
			}, {
				header : '模板内容',
				dataIndex : 'content',
				flex:1
			}, {
				header : '记录总数',
				width:70,
				dataIndex : 'totalcnt'
			},{
				header : '定时发送',
				dataIndex : 'istimertask',
				width:70,
				renderer:function(val){
					if(val)
						return '是';
					else
						return '否';
				}
			},{
				header:'定时发送时间',
				dataIndex:'sendtime',
				width:130
			},{
				header:'处理状态',
				dataIndex:'procstatus',
				width:100
			},{
				header:'创建人员',
				dataIndex:'createid',
				width:70
			},{
				header:'创建时间',
				dataIndex:'createdate',
				width:130
			},{
				header : '查看明细',
				xtype : 'actioncolumn',
				hidden:false,
				width:100,
				align:'center',
				items : [ {
					icon:'../../images/detail.gif',
					handler : function(grid, rowIndex, colIndex, item, e) {
						var taskid=grid.getStore().getAt(rowIndex).get('taskid');
						var procstatus=grid.getStore().getAt(rowIndex).get('procstatus');
						var istimertask=grid.getStore().getAt(rowIndex).get('istimertask');
						if(procstatus=='已处理'||istimertask==true){
							Ext.getCmp('confirm').setVisible(false);
						}else{
							Ext.getCmp('confirm').setVisible(true);
						}
						confirmtaskid=grid.getStore().getAt(rowIndex).get('taskid');
						prevWindow.show();
						taskDtlStore.getProxy().extraParams = {
							'taskid' : confirmtaskid
						};
						taskDtlStore.currentPage=1;
						taskDtlStore.load();
					}
				} ]
			},{
				header : '删除任务',
				xtype : 'actioncolumn',
				width:100,
				align:'center',
				renderer:function(value,meta,record){
					if(record.get('procstatus')=='待确认'||record.get('procstatus')=='待处理'){
						return "<img src='../../images/del3.gif'></img>"
					}else{
						return '';
					}
				},
				listeners:{
					click:function(grid, tmp,rowIndex, colIndex){
						var taskname=grid.getStore().getAt(rowIndex).get('taskname');
						var taskid=grid.getStore().getAt(rowIndex).get('taskid');
						var procstatus=grid.getStore().getAt(rowIndex).get('procstatus');
						if(procstatus=='已处理')
							return;
						Ext.Msg.show({
							msg:'确定删除任务【'+taskname+'】?', 
							title:'信息提示', 
							buttons:Ext.Msg.OKCANCEL,
							fn:function(btn, text){
								if (btn == 'ok'){
							    	var request=Ext.Ajax.request({
										params: {
									        taskid: taskid
									    },
										url:'../../action/pujjr/deleteBatchTask',
										success:function(response,options){
											Ext.Msg.alert('信息提示', '删除成功');
											gridStore.load();
										}
									});
							    }
							}
						})
					}
				}
			}],
			dockedItems : [ {
				xtype:'toolbar',
				dock:'top',
				items:['->',{
					glyph:'xf067@FontAwesome',
					text:'创建批量短信任务',
					handler:function(){
						queryPanel.hide();
						editPanel.anchor='100% 100%';
						editPanel.updateLayout();
						editPanel.show();
						editForm.getForm().reset();
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
			title:'批量短信任务管理',
			anchor:'100% 100%',
			items:[queryForm,queryGrid]
		});
		var editPanel =Ext.create('Ext.panel.Panel',{
			name:'editPanel',
			title:'批量短信任务管理',
			anchor:'100% 100%',
			bodyBorder:false,
			hidden:true,
			layout:'anchor',
			items:[editForm]
		});
		
		Ext.create('Ext.container.Viewport', {
			layout :'anchor',
			items : [queryPanel,editPanel],
			renderTo : Ext.getBody()
			
		})
	}
})

Ext.application({
	name : '发送单条短信',
	launch : function() {
		var editAction;
		var queryForm=Ext.create('Ext.form.Panel',{
			border:1,
			bodyPadding:'20 20 0',
			anchor:'100% 60%',
			paramsAsHash: true,

			xtype:'panel',
			border:false,
			margin:'10 0 0 10',
			layout : 'form',
			items : [{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'10 0 0 10',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textareafield',
						fieldLabel : '短信内容',
						name:'msg',
						width:600,
						height:150,
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'10 0 0 10',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '短信签名',
						name:'sign',
						width:600,
						value:'【潽金租赁】',
						allowBlank:false
						}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'10 0 0 10',
				items:[{
					columnWidth:0.8,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '接收电话号码',
						name:'telno',
						width:600,
						allowBlank:false
						}]
				}]
			}],
			buttonAlign:'center',
			buttons:[{
				text:'发送',
				glyph:'xf002@FontAwesome',
				handler:function(){
					if(queryForm.getForm().isValid()){
						if(new Ext.window.MessageBox().confirm('信息提示','确认发送短信？',function(btn){
							if(btn=='yes'){
								queryForm.getForm().submit({
									url:'../../action/pujjr/sendone2onemsg',
									success:function(form,action){
										Ext.Msg.alert('信息提示','发送成功，请稍候查询发送结果');
									},
									failure:function(form,action){
										Ext.Msg.alert('信息提示',action.result.errors.errmsg);
									}
								})
							}
							
							
						}));
						 
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
			          {name:'alias',type:'string'},
			          {name:'fullname',type:'string'},
			          {name:'openbankname',type:'number'},
			          {name:'openbankno',type:'string'},
			          {name:'address',type:'string'},
			          {name:'linkman',type:'string'},
			          {name:'mobile',type:'string'},
			          {name:'telno',type:'String'},
			          {name:'comment',type:'String'},
			          {name:'reserve1',type:'String'},
			          {name:'reserve2',type:'String'}],
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
				url : '../../action/risk/queryJXSList'
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
				margin:'10 0 0 10',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '经销商ID',
						name:'id',
						width:400,
						hidden:true,
						allowBlank:true
					},{
						xtype:'textfield',
						fieldLabel : '经销商简称',
						name:'alias',
						width:400,
						allowBlank:false
					}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '经销商全程',
						name:'fullname',
						width:400,
				        allowBlank:false,
					}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'0 0 0 10',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '开户银行名称',
						name:'openbankname',
						width:400,
						allowBlank:false
					}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '开户行账号',
						name:'openbankno',
						width:400,
						allowBlank:false
					}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'0 0 0 10',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '经销商地址',
						name:'address',
						width:400
					}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '联系人姓名',
						name:'linkman',
						width:400
					}]
				}]
			},{
				xtype:'panel',
				border:false,
				layout:'column',
				margin:'0 0 0 10',
				items:[{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '联系人手机',
						name:'mobile',
						width:400
					}]
				},{
					columnWidth:0.5,  //该列占用的宽度，标识为50％
					layout:'hbox',
					border:false,
					items:[{
						xtype:'textfield',
						fieldLabel : '联系人座机',
						name:'telno',
						width:400
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
								url:editAction==true?'../../action/risk/updateJXS':'../../action/risk/addJXS',
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
				header : '经销商简称',
				dataIndex : 'alias',
				width:150
			}, {
				header : '经销商全称',
				flex:1,
				dataIndex : 'fullname'
			}, {
				header : '开户银行名',
				dataIndex : 'openbankname',
				width:150
			},{
				header : '开户银行账号',
				dataIndex : 'openbankno',
				width:150
			},{
				header : '联系人',
				dataIndex : 'linkman',
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
					var name=grid.getStore().getAt(rowIndex).get('alias');
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
									url:'../../action/risk/deleteJXS',
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
					text:'增加经销商',
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
			title:'发送单条短信',
			anchor:'100% 100%',
			items:[queryForm]
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
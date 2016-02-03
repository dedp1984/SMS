package com.pujjr.tree;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxTreeNode extends TreeNode
{
	public  boolean checked=false;
	public  List<CheckBoxTreeNode> children=new ArrayList<CheckBoxTreeNode>();
}

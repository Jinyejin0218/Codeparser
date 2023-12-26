import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class AntiInfo {
   public CompilationUnit compilationUnit;

   public AntiInfo() {
      String fileName = "../sutda";
      File ffile = new File(fileName);
      File[] fileArray = ffile.listFiles();

      if (ffile.listFiles() != null) {
         for (int i = 0; i < fileArray.length; i++) {
            fileName = fileArray[i].toString();
            if (!fileArray[i].isFile()) {
               System.exit(1);
            } else {
               parse(fileArray[i]);
            }
         }
      } else {
         parse(ffile);
      }
   }

   HashMap<String, ArrayList<String>> map = Save_ClassInfo.map;
   HashMap<String, Integer> map_m = Save_ClassInfo.map_m;
   static HashMap<String, ArrayList<String>> map_anti = new HashMap<>();
   
   // class 이름 저장
   ArrayList<String> tt = Save_ClassInfo.tt;
   // method 이름 저장
   ArrayList<String> mm = Save_ClassInfo.mm;
   
   // anti method sloc > 25
   ArrayList<String> anti_msloc = new ArrayList<>();
   // anti god class method > 20
   ArrayList<String> anti_m = new ArrayList<>();
   // anti singleton
   ArrayList<String> anti_sin = new ArrayList<>();
   
   // anti long parameter > 5
   ArrayList<String> anti_para = new ArrayList<>();
   // no block
   ArrayList<String> anti_block = new ArrayList<>();
   
   public void parse(File file) {
      BufferedReader reader = null;
      try {
         reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      StringBuilder source = new StringBuilder();
      char[] buf = new char[10];
      int numRead = 0;

      try {
         while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            source.append(readData);
            buf = new char[1024];
         }
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      // 파일 읽기 끝 옵션 설정 시작
      ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(source.toString().toCharArray());
      parser.setStatementsRecovery(true);
      parser.setBindingsRecovery(true);
      parser.setResolveBindings(true);
      // 옵션끝
      Map<?, ?> options = JavaCore.getOptions();
      JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
      parser.setCompilerOptions(options);

      compilationUnit = (CompilationUnit) parser.createAST(null);
      startParser();
   }
   
   public void startParser() {
      List<AbstractTypeDeclaration> types = compilationUnit.types();

      for (AbstractTypeDeclaration type : types) {
         if (type instanceof TypeDeclaration) {
            typeDeclaration((TypeDeclaration) type);
         }
      }
      
      map_anti.put("godclass", anti_m);
      map_anti.put("msloc", anti_msloc);
      map_anti.put("singleton", anti_sin);
      map_anti.put("longpara", anti_para);
      map_anti.put("noblock", anti_block);
   }
   
   boolean anti1, anti2, anti3, anti4, anti5;
   String anti_field ="";
   
   String typename = "";

   // TYPE
   public void typeDeclaration(Object t) {
	  anti1 = false;	anti2 = false;	anti3 = false;	anti4 = false;	anti5 = false;
      
      TypeDeclaration type = (TypeDeclaration) t;
      typename = type.getName().toString();
      
      for (Object bodyDeclaration : type.bodyDeclarations()) {
          if (bodyDeclaration instanceof FieldDeclaration) {
             fieldDeclaration(bodyDeclaration);
          }
      }
      int god = 0;
      for (Object bodyDeclaration : type.bodyDeclarations()) {
          if (bodyDeclaration instanceof MethodDeclaration) {
             methodDeclaration(bodyDeclaration);
             god++;
          }
       }
      
      if(god>20) {
    	  anti_m.add(typename);
      }
      
      if(anti1&&anti2&&anti3&&anti4&&anti5) {
    	  anti_sin.add(typename);
      }
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof TypeDeclaration) {
            typeDeclaration(bodyDeclaration);
         }
      }
   }
   // FIELD
   public void fieldDeclaration(Object bodyDeclaration) {
	   FieldDeclaration field = (FieldDeclaration) bodyDeclaration;
	   
	   String fieldType = field.getType().toString().replace(">", "&gt;").replace("<", "&lt;");

	   int an1 = 0;
	   for (int i = 0; i < field.modifiers().size(); i++) {
	          String Modifier = field.modifiers().get(i).toString();
	          if (Modifier.equals("static"))
	        	 an1++;
	          else if (Modifier.equals("private"))
	             an1++;
	   }
	   
	   if(an1==2) {
		   anti1=true;
	   }
	   
	   if(typename.equals(fieldType)) {
		   anti2 = true;
       }
	   
		try {
			if (field.fragments().get(0) instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) field.fragments().get(0);
				String fieldname = vdf.getName().toString();
				if (an1==2 && typename.equals(fieldType)) {
					anti_field = fieldname;
				}
			}
		} catch (Exception e) {
			throw e;
		}
   }
   String methodname = "";
   // METHOD
   public void methodDeclaration(Object m) {
      MethodDeclaration md = (MethodDeclaration) m;
      methodname = md.getName().toString();
      m_sloc=0;
      get_Block(md.getBody());
      
      if (m_sloc > 25) {
    	  anti_msloc.add(typename+methodname);
      }
      
	   int an1 = 0;
	   for (int i = 0; i < md.modifiers().size(); i++) {
	          String Modifier = md.modifiers().get(i).toString();
	          if (Modifier.equals("static"))
	        	 an1++;
	          else if (Modifier.equals("public"))
	             an1++;
	   }
	   
	   if(an1==2) {
		   anti3=true;
	   }
	   if(md.parameters().size() > 5) {
		   anti_para.add(typename+methodname);
	   }
   }
   
   int m_sloc = 0;

   public void get_Block(Object o) {
	      // {중괄호}가 있는 경우 Block이 생성되므로 내부에 있는 statements들을 하나씩 분석
	      if (o instanceof Block) {
	         Block b = (Block) o;
	         List<Statement> ls = b.statements();
	         for (Statement s : ls) {
	        	 get_Statement(s);
	         }
	      }
	      // {중괄호}가 없는 경우 Block이 생성되지않으므로 바로 statement 분석
	      else if (o instanceof Statement) {
	    	 anti_block.add(typename+methodname);
	         get_Statement(o);         
	      }
	   }

	   // Body내의 Statements 종류에 따라 출력하는 함수
	   public void get_Statement(Object o) {
	      // VariableDeclarationStatement (변수선언)
	      if (o instanceof VariableDeclarationStatement) {
	         VariableDeclarationStatement vds = (VariableDeclarationStatement) o;
	         for (int i = 0; i < vds.modifiers().size(); i++) {
	            String Modifier = vds.modifiers().get(i).toString();
	         }

	         get_Fragment(vds.fragments().get(0));

	         // ExpressionStatement (대입식)
	      } else if (o instanceof ExpressionStatement) {
	         ExpressionStatement es = (ExpressionStatement) o;
	         get_Assignment(es.getExpression());

	         // ForStatement (For문)
	      } else if (o instanceof ForStatement) {
	         ForStatement fs = (ForStatement) o;
	         // 초기식
	         VariableDeclarationExpression vde = (VariableDeclarationExpression) fs.initializers().get(0);
	         get_Fragment(vde.fragments().get(0));

	         // 조건식
	         InfixExpression ie = (InfixExpression) fs.getExpression();
	         get_Expression(ie);

	         // 증감식
	         get_Assignment(fs.updaters().get(0));

	         // 실행문
	         get_Block(fs.getBody());
	         
	         //EnhancedForStatement(For in문)
	      } else if (o instanceof EnhancedForStatement) {
	    	  EnhancedForStatement efs = (EnhancedForStatement) o;    	  
			  anti_block.add(typename+methodname);
			  
		      // 실행문
		      get_Block(efs.getBody());
		      
	         // IfStatement (If문)
	      } else if (o instanceof IfStatement) {
	         IfStatement is = (IfStatement) o;

	         // if 조건문
	         get_Expression(is.getExpression());

	         // 실행문
	         get_Block(is.getThenStatement());

	         // else 실행
	         if (is.getElseStatement() != null) {
	            get_Block(is.getElseStatement());
	         }
	         
	      // WhileStatement (While문)
	      } else if (o instanceof WhileStatement) {
	         WhileStatement ws = (WhileStatement) o;
	         // 조건문
	         get_Expression(ws.getExpression());

	         // 실행문
	         get_Block(ws.getBody());
	         
	      } else if(o instanceof ReturnStatement) {
	         ReturnStatement rr = (ReturnStatement) o;
	         Expression e = (Expression)rr.getExpression();
	         if(e.toString().equals(anti_field)) {
	            anti5=true;
	         }
	         //rs.add(e.toString());
	      }
	      m_sloc++;
	   }
	   // Fragment (변수 선언 또는 대입식 또는 연산식)
	   public void get_Fragment(Object o) {
	      if (o instanceof VariableDeclarationFragment) {
	         VariableDeclaration vdf = (VariableDeclarationFragment) o;
	         String fieldName = vdf.getName().toString().replaceAll(" ", "");

	         // 연산식일 경우
	         if (vdf.getInitializer() instanceof InfixExpression) {
	            // InfixExpression(연산식)을 사용하기 위해 getExpression() 함수 호출
	            get_Expression(vdf.getInitializer());
	         } 
	         /** instance 생성!!! **/
	         
	         else if (vdf.getInitializer() instanceof ClassInstanceCreation) {
	            ClassInstanceCreation cic = (ClassInstanceCreation) vdf.getInitializer();
	            //System.out.println("객체 name: " + fieldName);   //instance
	            //System.out.println("객체 type: "+cic.getType());   //test1
	            //it.add(fieldName);
	         }
	         // 값을 대입할 경우
	         else if (vdf.getInitializer() != null) {
	            // System.out.println("name: " + fieldName);
	            String fieldToken = vdf.getInitializer().toString();
	            // System.out.println("value: " + fieldToken);
	         } else {
	            //bad = 1;
	            // System.out.println("name: " + fieldName);
	         }
	      }
	   }

	   public void get_Expression(Object o) {
	      if (o instanceof InfixExpression) {
	         InfixExpression ie = (InfixExpression) o;
	         String left2 = ie.getLeftOperand().toString();
	         String oper2 = ie.getOperator().toString();
	         String right2 = ie.getRightOperand().toString();
	      } else if(o instanceof ClassInstanceCreation){
	         ClassInstanceCreation cic = (ClassInstanceCreation)o;
	         if(typename.equals(cic.getType().toString())) {
	            anti5=true;
	         }
	      }
	      else {
	         // System.out.println("right_hand_side: "+o.toString());
	      }
	   }
	   public void get_Assignment(Object o) {
	      if (o instanceof Assignment) {
	         Assignment ag = (Assignment) o;
	         String left1 = ag.getLeftHandSide().toString().replace(" ", "");
	         String oper1 = ag.getOperator().toString().replace(" ", "");

	         Expression ife = (Expression) ag.getRightHandSide();
	         get_Expression(ife);
	         
	         //System.out.println("left: "+left1);
	         //System.out.println("anti_field: "+anti_field);
	         if(left1.equals(anti_field)) {
	            anti4=true;
	         }
	         
	      } else if (o instanceof MethodInvocation) {
	         MethodInvocation mi = (MethodInvocation) o;
	         // System.out.println("expression: "+mi.getExpression()); //TODO: System 과 out
	         // 분리 못함
	         // System.out.println("name: "+mi.getName().getIdentifier());
	         for (int i = 0; i < mi.arguments().size(); i++) {
	            // System.out.println("argument: "+mi.arguments().get(i));
	         }
	      }
	   }
}

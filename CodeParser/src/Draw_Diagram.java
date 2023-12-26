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
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
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

public class Draw_Diagram {
   public CompilationUnit compilationUnit;

   public Draw_Diagram() {
      String fileName = "../sutda";
      File ffile = new File(fileName);
      File[] fileArray = ffile.listFiles();

      System.out.println("digraph {");
      if (ffile.listFiles() != null) {
         for (int i = 0; i < fileArray.length; i++) {
            fileName = fileArray[i].toString();
            if (!fileArray[i].isFile()) {
               System.out.println(fileArray[i].getAbsolutePath() + "does not exist.");
               System.exit(1);
            } else {
               parse(fileArray[i]);
            }
         }
      } else {
         parse(ffile);
      }
      Draw_Relation dr = new Draw_Relation();
      dr.draw_relation(relation);

      System.out.print("}");
   }

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
      Save_ClassInfo sc = new Save_ClassInfo();
      map = sc.map;
      map_m = sc.map_m;
      
      AntiInfo ai = new AntiInfo();
      anti_map = ai.map_anti;
      List<AbstractTypeDeclaration> types = compilationUnit.types();
      // 박스 도식
      for (AbstractTypeDeclaration type : types) {
         if (type instanceof TypeDeclaration) {
            typeDeclaration((TypeDeclaration) type);
         }
      }
      //Draw_Relation dr = new Draw_Relation();
      //dr.draw_relation(relation);
   }

   HashMap<String, ArrayList<String>> map = new HashMap<>();
   HashMap<String, Integer> map_m = new HashMap<>();
   HashMap<String, ArrayList<String>> anti_map = new HashMap<>();
   String[][] relation = new String[100][3];
   String typename = "";

   // TYPE
   public void typeDeclaration(Object t) {
      TypeDeclaration type = (TypeDeclaration) t;
      typename = type.getName().toString();
      System.out.print(typename + "[shape=\"plaintext\",");

      if(anti_map.get("singleton").contains(typename)) {
    	  System.out.print("style=\"filled\", fillcolor=skyblue, ");    	  
      }
      
      System.out.print("label=<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">\");\r\n");

      System.out.print("\t<TR><TD>");
      for (int i = 0; i < type.modifiers().size(); i++) {
         String Modifier = type.modifiers().get(i).toString();
         boolean Interface = type.isInterface();
         if (Interface) {
            System.out.print(" interface ");
         }
      }
      if (anti_map.get("godclass").contains(typename)) {
         System.out.print("<FONT COLOR=\"RED\">" + typename + "</FONT>");
      } else {
         System.out.print(typename);
      }
      
      System.out.println("</TD></TR>");

      System.out.print("\t<TR><TD>");
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof FieldDeclaration) {
            fieldDeclaration(bodyDeclaration);
            System.out.print("<br/>\n\t");
         }
      }
      System.out.println("</TD></TR>");

      System.out.print("\t<TR><TD>");
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof MethodDeclaration) {
            methodDeclaration(bodyDeclaration);
            System.out.print("<br/>\n\t");
         }
      }
      System.out.println("</TD></TR>");
      System.out.println("\t</TABLE>>]");

      // 실체화 관계 (interface)
      for (Object o : type.superInterfaceTypes()) {
         save_rel(typename, o.toString(), "realization");
      }
      // 일반화 관계 (상속)
      if (type.getSuperclassType() != null) {
         String typesuperclass = type.getSuperclassType().toString();
         save_rel(typename, typesuperclass, "generalization");
      }
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof TypeDeclaration) {
            TypeDeclaration t2 = (TypeDeclaration) bodyDeclaration;
            save_rel(t2.getName().toString(), typename, "composition");
            typeDeclaration(bodyDeclaration);
         }
      }
   }

   // FIELD
   public void fieldDeclaration(Object bodyDeclaration) {
      FieldDeclaration field = (FieldDeclaration) bodyDeclaration;

      for (int i = 0; i < field.modifiers().size(); i++) {
         String Modifier = field.modifiers().get(i).toString();
         if (Modifier.equals("public"))
            System.out.print("+");
         else if (Modifier.equals("private"))
            System.out.print("-");
         else if (Modifier.equals("protected"))
            System.out.print("#");
      }

      String fieldType = field.getType().toString();
      fieldType = fieldType.replace(">", "&gt;").replace("<", "&lt;");
      
      try {
         if (field.fragments().get(0) instanceof VariableDeclarationFragment) {
            VariableDeclarationFragment vdf = (VariableDeclarationFragment) field.fragments().get(0);
            String fieldname = vdf.getName().toString();
            System.out.print(fieldname);
            System.out.print(": " + fieldType);
         }
      } catch (Exception e) {
         throw e;
      }
      
      if (map.get("typelist").contains(fieldType)) {
         save_rel(fieldType, typename, "direct association");
      }
   }

   // METHOD
   public void methodDeclaration(Object bodyDeclaration) {
      MethodDeclaration method = (MethodDeclaration) bodyDeclaration;
      String methodname = method.getName().toString();

      get_Block(method.getBody());
      
      String a = typename+methodname;
      if (anti_map.get("msloc").contains(a)) {
         System.out.print("<FONT COLOR=\"BLUE\">");
      }
 
      for (int i = 0; i < method.modifiers().size(); i++) {
         String Modifier = method.modifiers().get(i).toString();
         if (Modifier.equals("public"))
            System.out.print("+");
         else if (Modifier.equals("private"))
            System.out.print("-");
         else if (Modifier.equals("protected"))
            System.out.print("#");
      }

      System.out.print(methodname + "(");

      int p = 0;
      for (Object methodpara : method.parameters()) {
         SingleVariableDeclaration method_para = (SingleVariableDeclaration) methodpara;
         String paraname = method_para.getName().toString();
         String paratype = method_para.getType().toString();
         if (map.get("typelist").contains(paratype)) {
            save_rel(typename, paratype, "dependency");
         }
         System.out.print(paraname + ":" + paratype + "");
         p++;
         if (p < method.parameters().size()) {
            System.out.print(", ");
         }
      }
      System.out.print(")");

      if (method.getReturnType2() != null) {
         String returntype = method.getReturnType2().toString();
         returntype = returntype.replace(">", "&gt;").replace("<", "&lt;");
         System.out.print(": " + returntype);
      }
      // </FONT> - FONT COLOR 닫기
      if (anti_map.get("msloc").contains(a)) {
         System.out.print("</FONT>");
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
      } else if (o instanceof WhileStatement) {
         WhileStatement ws = (WhileStatement) o;
         // 조건문
         get_Expression(ws.getExpression());

         // 실행문
         get_Block(ws.getBody());
      } else if(o instanceof ReturnStatement) {
         ReturnStatement rr = (ReturnStatement) o;
         Expression e = (Expression)rr.getExpression();
         //rs.add(e.toString());
      }
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
   int r = 0;

   public void save_rel(String f1, String f2, String rel) {
      relation[r][0] = f1;
      relation[r][1] = f2;
      relation[r][2] = rel;
      r++;
   }
}

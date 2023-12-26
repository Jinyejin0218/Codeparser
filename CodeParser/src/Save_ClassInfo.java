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

public class Save_ClassInfo {
   public CompilationUnit compilationUnit;

   public Save_ClassInfo() {
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


   static HashMap<String, ArrayList<String>> map = new HashMap<>();	//key: typelist,	methodlist,		typename - method
   static HashMap<String, Integer> map_m = new HashMap<>();

   // class 이름 저장
   static ArrayList<String> tt = new ArrayList<String>();
   // method 이름 저장
   static ArrayList<String> mm = new ArrayList<String>();
   
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
      map.put("typelist", tt);
   }

   String typename = "";

   // TYPE
   public void typeDeclaration(Object t) {
      ArrayList<String> m = new ArrayList<String>();
      
      TypeDeclaration type = (TypeDeclaration) t;
      typename = type.getName().toString();
      tt.add(typename);   
      
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof MethodDeclaration) {
            MethodDeclaration method = (MethodDeclaration)bodyDeclaration;
            // Method Name
            String methodname = method.getName().toString();
            m.add(methodname);
            mm.add(methodname);
         }
      }
      map.put(typename, m);
      map.put("methodlist", mm);
      for (Object bodyDeclaration : type.bodyDeclarations()) {
         if (bodyDeclaration instanceof TypeDeclaration) {
            typeDeclaration(bodyDeclaration);
         }
      }
   }
}

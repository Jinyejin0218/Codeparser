

public class Draw_Relation {
   //의존, 집합, 합성 이상함
   
   public void draw_relation(String[][] relation) {
      int i=0;
      int j=0;
      while(relation[i][0]!=null) {   
         String f1=relation[i][0];
         String f2=relation[i][1];
         String rel=relation[i][2];
         
         if(rel.equals("realization")) {            //실체화
            System.out.println(f1+"->"+f2+"[dir=forward style=dashed arrowhead=empty]");
         }
         else if(rel.equals("generalization")) {      //일반화
            System.out.println(f1+"->"+f2+"[dir=forward style=solid arrowhead=empty]"); 
         }
         else if(rel.equals("aggregation")) {      //집합
            System.out.println(f1+"->"+f2+"[dir=forward style=solid arrowhead=odiamond]");
         }
         else if(rel.equals("dependency")) {         //의존
            //채워야함
            System.out.println(f1+"->"+f2+"[dir=forward style=dashed arrowhead=vee]");
         }
         else if(rel.equals("composition")) {      //합성
            System.out.println(f1+"->"+f2+"[dir=forward style=solid arrowhead=diamond]");
         }
         else if(rel.equals("direct association")){   //직접연관
            System.out.println(f1+"->"+f2+"[dir=forward style=solid arrowhead=vee]");
         }
         else if(rel.equals("association")) {      //연관
            System.out.println(f1+"->"+f2+"[dir=none]");
         }
         
         i++;
      }      
   }
}
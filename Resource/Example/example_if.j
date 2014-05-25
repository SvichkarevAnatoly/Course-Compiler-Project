.class public test
.super java/lang/Object
.method public <init>()V
   .limit stack 1
   .limit locals 1
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

.method public static main([Ljava/lang/String;)V
   .limit stack 100
   .limit locals 3
   sipush 5
   istore_1
   
   sipush 8
   istore_2
   
   ;if( loc1 <= loc2 )
   iload_1
   iload_2
   
   if_icmpgt else
   sipush 6
   istore_1
   goto endif
else:
   sipush 7
   istore_1
endif:
   
   getstatic java/lang/System/out Ljava/io/PrintStream;
   iload_1
   invokevirtual java/io/PrintStream/println(I)V
   return
.end method

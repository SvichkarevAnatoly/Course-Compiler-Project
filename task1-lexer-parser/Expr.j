.class public Expr
.super java/lang/Object
.method public <init>()V
   .limit stack 1
   .limit locals 1
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method
.method public static main([Ljava/lang/String;)V
   .limit stack 10
   .limit locals 2
   sipush 126
   sipush 42
   idiv
   sipush 7
   sipush 5
   isub
   ineg
   imul
   sipush 8
   sipush 2
   idiv
   sipush 2
   idiv
   sipush 2
   idiv
   isub
   sipush 10
   sipush 3
   sipush 3
   imul
   isub
   iadd
   sipush 1
   iadd
   istore_1
   getstatic java/lang/System/out Ljava/io/PrintStream;
   iload_1
   invokevirtual java/io/PrintStream/println(I)V
   return
.end method

; ModuleID = 'test5.c'
source_filename = "test5.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

; Function Attrs: noinline nounwind uwtable
define i32 @main() #0 {
entry:
  %retval = alloca i32, align 4
  %a = alloca i32, align 4
  %b = alloca i32, align 4
  %c = alloca i32, align 4
  %d = alloca i32, align 4
  %e = alloca i32, align 4
  %f = alloca i32, align 4
  %g = alloca i32, align 4
  store i32 0, i32* %retval, align 4
  store i32 3, i32* %a, align 4
  store i32 4, i32* %b, align 4
  store i32 5, i32* %c, align 4
  store i32 6, i32* %d, align 4
  %0 = load i32, i32* %a, align 4
  %cmp = icmp eq i32 %0, 3
  br i1 %cmp, label %if.then, label %if.else3

if.then:                                          ; preds = %entry
  %1 = load i32, i32* %b, align 4
  %cmp1 = icmp eq i32 %1, 4
  br i1 %cmp1, label %if.then2, label %if.else

if.then2:                                         ; preds = %if.then
  store i32 0, i32* %e, align 4
  br label %if.end

if.else:                                          ; preds = %if.then
  store i32 1, i32* %e, align 4
  br label %if.end

if.end:                                           ; preds = %if.else, %if.then2
  store i32 15, i32* %f, align 4
  br label %if.end12

if.else3:                                         ; preds = %entry
  %2 = load i32, i32* %c, align 4
  %cmp4 = icmp eq i32 %2, 5
  br i1 %cmp4, label %if.then5, label %if.else10

if.then5:                                         ; preds = %if.else3
  store i32 2, i32* %e, align 4
  %3 = load i32, i32* %d, align 4
  %cmp6 = icmp eq i32 %3, 5
  br i1 %cmp6, label %if.then7, label %if.else8

if.then7:                                         ; preds = %if.then5
  store i32 10, i32* %e, align 4
  br label %if.end9

if.else8:                                         ; preds = %if.then5
  store i32 9, i32* %f, align 4
  br label %if.end9

if.end9:                                          ; preds = %if.else8, %if.then7
  br label %if.end11

if.else10:                                        ; preds = %if.else3
  store i32 20, i32* %e, align 4
  br label %if.end11

if.end11:                                         ; preds = %if.else10, %if.end9
  br label %if.end12

if.end12:                                         ; preds = %if.end11, %if.end
  %4 = load i32, i32* %retval, align 4
  ret i32 %4
}

attributes #0 = { noinline nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 6.0.0 (http://llvm.org/git/clang.git 3685dbaf4a6514f55565fd952312d143945a6d49) (http://llvm.org/git/llvm.git a3b8ce30a4f529175b6f723d6c622eb7e78b03b0)"}

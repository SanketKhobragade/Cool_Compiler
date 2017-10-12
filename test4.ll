; ModuleID = 'test4.cpp'
source_filename = "test4.cpp"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-unknown-linux-gnu"

%class.A = type { i32, i32 }


; Function Attrs: noinline norecurse uwtable
define i32 @main() #0 {
entry:
  %retval = alloca i32, align 4

  %obj = alloca %class.A, align 4
  %obj2 = alloca %class.A, align 4
  %this.addr = alloca %class.A*, align 8
  store %class.A* %obj, %class.A** %this.addr, align 8
  %this1 = load %class.A*, %class.A** %this.addr, align 8
  %result = alloca i32, align 4
  %val1 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 0
  %val2 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 1
  store i32 1, i32* %val1, align 4
  store i32 2, i32* %val2, align 4
  %0 = load i32, i32* %val1, align 4
  %1 = load i32, i32* %val2, align 4
  ret i32 %0
}

; Function Attrs: noinline nounwind uwtable
define void @_ZN1A9set_valueEi(%class.A* %this, i32 %n) {
entry:
  %this.addr = alloca %class.A*, align 8
  %n.addr = alloca i32, align 4
  store %class.A* %this, %class.A** %this.addr, align 8
  store i32 %n, i32* %n.addr, align 4
  %this1 = load %class.A*, %class.A** %this.addr, align 8
  %0 = load i32, i32* %n.addr, align 4
  %val1 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 0
  store i32 %0, i32* %val1, align 4
  ret void
}

; Function Attrs: noinline nounwind uwtable
define i32 @_ZN1A12update_valueEi(%class.A* %this, i32 %x) {
entry:
  %this.addr = alloca %class.A*, align 8
  %x.addr = alloca i32, align 4
  store %class.A* %this, %class.A** %this.addr, align 8
  store i32 %x, i32* %x.addr, align 4
  %this1 = load %class.A*, %class.A** %this.addr, align 8
  %val2 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 1
  %0 = load i32, i32* %val2, align 4
  %1 = load i32, i32* %x.addr, align 4
  %add = add nsw i32 %0, %1
  %val1 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 0
  store i32 %add, i32* %val1, align 4
  %val22 = getelementptr inbounds %class.A, %class.A* %this1, i32 0, i32 1
  %2 = load i32, i32* %val22, align 4
  ret i32 %2
}

attributes #0 = { noinline norecurse uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { noinline nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="true" "no-frame-pointer-elim-non-leaf" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 6.0.0 (http://llvm.org/git/clang.git 3685dbaf4a6514f55565fd952312d143945a6d49) (http://llvm.org/git/llvm.git a3b8ce30a4f529175b6f723d6c622eb7e78b03b0)"}

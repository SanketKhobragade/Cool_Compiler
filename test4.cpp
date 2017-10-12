class A{
	public :
		int val1, val2,val3;

	public : 

		void set_value(int n){
			val1 = n;
		}
		int update_value(int x){
			val1 = val2 + x;
			return val2;
		}
};

int main(){
	A obj;
	A obj2;
	obj.set_value(10);
	obj2.set_value(20);
	int result = obj.update_value(13);
	return 0;
}
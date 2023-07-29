////////////////////////////////////////////////////////////
//  Date : 2023.01.27 coded
//  Problem : 1049
//  Language : C++
//  by Kwanwoo Kim                                                      
////////////////////////////////////////////////////////////    

/*
문제
Day Of Mourning의 기타리스트 강토가 사용하는 기타에서 N개의 줄이 끊어졌다. 따라서 새로운 줄을 사거나 교체해야 한다. 강토는 되도록이면 돈을 적게 쓰려고 한다. 6줄 패키지를 살 수도 있고, 1개 또는 그 이상의 줄을 낱개로 살 수도 있다.

끊어진 기타줄의 개수 N과 기타줄 브랜드 M개가 주어지고, 각각의 브랜드에서 파는 기타줄 6개가 들어있는 패키지의 가격, 낱개로 살 때의 가격이 주어질 때, 적어도 N개를 사기 위해 필요한 돈의 수를 최소로 하는 프로그램을 작성하시오.

입력
첫째 줄에 N과 M이 주어진다. N은 100보다 작거나 같은 자연수이고, M은 50보다 작거나 같은 자연수이다. 둘째 줄부터 M개의 줄에는 각 브랜드의 패키지 가격과 낱개의 가격이 공백으로 구분하여 주어진다. 가격은 0보다 크거나 같고, 1,000보다 작거나 같은 정수이다.

출력
첫째 줄에 기타줄을 적어도 N개 사기 위해 필요한 돈의 최솟값을 출력한다.
*/

#include <iostream>
#define MAX_N 100
#define MAX_M 50
#define MAX_P 1000
using namespace std;

int _min(int a, int b){
    if(a<b) return a;
    return b;
}

int main(void){
    int n, m;
    int minPackagePrice=MAX_P, minIndivPrice = MAX_P;
    int price = 0;
    cin >>n >> m;
    int tp1, tp2;
    for(int k=0;k<m;k++){
        cin >> tp1 >> tp2;
        if(tp1 < minPackagePrice) minPackagePrice = tp1;
        if(tp2 < minIndivPrice) minIndivPrice = tp2;
    }

    price += n / 6 * minPackagePrice + _min(minPackagePrice, n % 6 * minIndivPrice);
    cout << price;
}

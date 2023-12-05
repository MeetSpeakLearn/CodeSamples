// Arithmatic.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include "pch.h"
#include "framework.h"
#include "Arithmatic.h"
#include "decimal_arithmetic.h"
#include "Integer.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// The one and only application object

CWinApp theApp;

using namespace std;

int main()
{
    int nRetCode = 0;

    HMODULE hModule = ::GetModuleHandle(nullptr);

    if (hModule != nullptr)
    {
        // initialize MFC and print and error on failure
        if (!AfxWinInit(hModule, nullptr, ::GetCommandLine(), 0))
        {
            // TODO: code your application's behavior here.
            wprintf(L"Fatal Error: MFC initialization failed\n");
            nRetCode = 1;
        }
        else
        {
            // TODO: code your application's behavior here.
            //testNibbles();
            //testAddDigitVectors();
            
            //const char* test[] { "100", "101" , "1000"};
            //testSumDigitVectors(test, 3);

            //testHasLeadingZeros();

            //testTrimDigitVector();
 
            // testMultiplyDigitVectors();

            // test_dv_int();

            // test_scratch_pad_delta();

            Integer i1("-1234");
            Integer i2("-10");
            Integer i3 = i1 + i2;
            std::int64_t i4 = 2;
            Integer i5 = i3 + i4;
            Integer i6 = i1 - i2;
            Integer i7 = i1 - i1;
            Integer i8 = i1 + i1;
            Integer i9(365);
            Integer i10(2);
            Integer i11 = i9 * i10;
            Integer i12(-1);
            Integer i13 = i9 / i10;
            Integer i14 = i9 % i10;

            cout << "i1=";
            cout << i1;
            cout << "\n";

            cout << "i2=";
            cout << i2;
            cout << "\n";

            cout << "i1+i2=";
            cout << i3;
            cout << "\n";

            cout << "i4=";
            cout << i4;
            cout << "\n";

            cout << "i3+i4=";
            cout << i5;
            cout << "\n";

            cout << "i1-i2=";
            cout << i6;
            cout << "\n";

            cout << "i1-i1=";
            cout << i7;
            cout << "\n";

            cout << "i1+i1=";
            cout << i8;
            cout << "\n";

            cout << "i9=";
            cout << i9;
            cout << "\n";

            cout << "i10=";
            cout << i10;
            cout << "\n";

            cout << "i9*i10=";
            cout << i11;
            cout << "\n";

            cout << "i12=";
            cout << i12;
            cout << "\n";

            cout << "i9/i10=";
            cout << i13;
            cout << "\n";

            cout << "i9%i10=";
            cout << i14;
            cout << "\n";

            cout << "fin\n";
        }
    }
    else
    {
        // TODO: change error code to suit your needs
        wprintf(L"Fatal Error: GetModuleHandle failed\n");
        nRetCode = 1;
    }

    return nRetCode;
}

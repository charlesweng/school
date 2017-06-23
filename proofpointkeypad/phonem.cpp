#include "phoneh.h"
int main(int argc, char** argv)
{
  int **parsedNos = parse(argc, argv); 
  cout << "adjacency matrix version" << endl;
  for (int i = 0; i < argc-1; i++)
  {
    cout << "Phone Number " << argv[i+1] << " is ";
    if(easyPhoneNumber(i,parsedNos[i]))
    {
      cout << "easy." << endl;
    }
    else
    {
      cout << "hard." << endl;
    }
  }
  cout << "adjacency list version" << endl;
  makeKeyList();
  for (int i = 0; i < argc-1; i++)
  {
    cout << "Phone Number " << argv[i+1] << " is ";
    if(easyPhoneNumber2(i,parsedNos[i]))
    {
      cout << "easy." << endl;
    }
    else
    {
      cout << "hard." << endl;
    }
  }
  return 0;
}

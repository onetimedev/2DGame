#include <Xlib.h>

class a {
  public:
  a() {
    XInitThreads();
  }
};

a X;

#include <dlfcn.h>

extern "C" typedef int (*XInitThreads_t)(void);

class a {
  public:
  a() {
    void *lib = dlopen("libX11.so", RTLD_LAZY);
    XInitThreads_t xInitThreads = (XInitThreads_t)dlsym(lib, "XInitThreads");
    xInitThreads();
    dlclose(lib);
  }
};

a X;

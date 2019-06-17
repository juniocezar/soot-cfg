# Soot Control Flow Graph

This is a simple Soot Extension to visualize the CFG of a Java application. 

## Installation

* First, download Soot 3.0.0 from:
[https://soot-build.cs.uni-paderborn.de/public/origin/master/soot/soot-master/3.0.0/build/sootclasses-trunk-jar-with-dependencies.jar](https://soot-build.cs.uni-paderborn.de/public/origin/master/soot/soot-master/3.0.0/build/sootclasses-trunk-jar-with-dependencies.jar) or build it yourself.

* You will need to copy Soot *.jar* file inside the **jar-libs** folder.

Build commands:

```bash
$ make build #this will generate the classes in the bin subdir
$ make run FILE=calc # FILE specifies the class to be analyzed
$ make pdf # the generated charts will be in the *pdfs* subdir

# or just run:
$ make all FILE=calc

```

## 

Inside the pdfs subdir you should have charts like:

![alt text](https://scontent-dfw5-2.xx.fbcdn.net/v/t1.0-9/64212555_10205850748024471_4623087378481807360_n.jpg?_nc_cat=101&_nc_oc=AQm04XT9_xy68qhouqYFTV7_eUWXScHw7Rz2a_cNL2IaQvUS3M3ZuUqdfB_rRUWnTk4&_nc_ht=scontent-dfw5-2.xx&oh=3ea5163258eb691adf13d73915fd7b2c&oe=5D83A042 "CFG for calc")


## License
GPL

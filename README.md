# Implementation of distributed, actor based, content-based recomendation system.

System is comprised of three main modules. First is scala actor based backend server that allows to deploy cluster of actor workerks.
This is for finding recomended products. Second module is web appliocation that is a GUI for client. This is kind of online shop allowing seraching and review products.
Last module is 'helper' module for preparing data to be analyzed.

Webapp and preprocessing module are written in Python and actor based server is written in Scala (+Akka).

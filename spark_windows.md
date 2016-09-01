# Running Apache Spark on Windows

The official Hadoop release does not include the native libraries to work properly on Windows. 
So when you try to run spark on your Windows machine, you may receive a `java.lang.NullPointerException`. 
This issue is caused by a missing `winutils.exe` file that Spark needs to use hadoop functionality.

To resolve this problem, you need to:

 * Download Hadoop 2.6.0 compiled for Windows(64-bit) from here: https://www.barik.net/archive/2015/01/19/172716/ 
 (download link `hadoop-2.6.0.tar.gz`). NOTE: there is a different winutils.exe file for the 64-bit Windows and it 
 will not work on the 32-bit OS.

 * Unzip the archive and copy the downloaded file winutils.exe into a folder like C:\hadoop\bin

 * Set up HADOOP_HOME environment variable pointing to the above directory (without bin): https://java.com/en/download/help/path.xml. For example: if you copied the winutils.exe to C:\hadoop\bin, set HADOOP_HOME to C:\hadoop

 * If you are running you Spark application in Eclipse, restart Eclipse 

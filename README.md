Excel sheets in `resources/sheets/`

Make sure you have Java installed.
Setup selenium grid locally -
[Download](https://www.seleniumhq.org/download/) `Selenium Standalone Server`.
`cd` to this directory.

To start hub - `java -jar selenium-server-standalone-3.141.59.jar -role hub`
On terminal you'll see "Nodes should register to http://<ip or localhost>:4444/grid/register/"

To start node - `java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://<ip or localhost>:4444/grid/register/`

RUN: `DriverScript.java` as a `Java Application`

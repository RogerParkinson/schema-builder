schema-builder
==============

Use this to migrate your database to [HyperJAXB3](https://hyperjaxb3.dev.java.net/). It will create the necessary XSD file to contain your database definitions. It also has a good example of using XJC to generate your entity classes using HyperJAXB3.

I've tested it with Oracle 10 and it probably works with other databases just as well. It *doesn't* work with HSQLDB. Datatype support is limited to the common ones. I'm keen to hear of any gaps there. 

The example XJC which generates JPA entity classes from the XSD injects a Cache annotation into the entity classes. 
This is actually Hibernate dependent. You don't have to inject the Cache settings as the example does, and this is the only [Hibernate](http://www.hibernate.org/) dependency, the rest is pure JPA. To see where this happens look at sandbox.xjb

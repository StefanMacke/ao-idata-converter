# ao-idata-converter
A converter for Java POJOs into/from webMethods' IData format.

When calling services in webMethods' Integration Server from Java, you need to use the internal data structure for the service pipeline called IData. However, to create this structure manually for every service can be quite annoying. Here's an example for only the output parameters of `pub.date.formatDate`:

    IData out = IDataFactory.create();
    IDataCursor idc = out.getCursor();
    idc.insertAfter("pattern", getString("pattern"));
    idc.insertAfter("timezone", getString("timezone"));
    idc.insertAfter("locale", getString("locale"));
    idc.destroy();

If you need to provide or receive complex nested documents, the code gets dirty very quickly.

What we want is a way to pass POJOs (plain old Java objects) directly to the service and also get them back from the service. `ao-idata-converter` provides exactly that: two helper methods that convert (complex nested) POJOs to IData and vice versa. It uses reflection to read/write the objects' attributes and convert them to/from IData documents.

## Conversion from POJO to IData

Every readable (`public` or via Getter) attribute of the POJO gets converted to IData. The attribute's name determines its pipeline name in IData. However, you can also override the pipeline names individually. Here's an example POJO:

    public class Address
    {
        private String street;

        public String ZipCode;

        @PipelineName("CityName")
        private String city;

        public String getStreet() { return street; }

        public void setStreet(String street) { this.street = street; }

        public String getCity() { return city; }

        public void setCity(final String city) { this.city = city; }
    }

A simple method call to `new ObjectConverter().convertToIData("address", addressObject)` will convert the `Address` object to the following IData:

    DOCUMENT "address"
    address ==> DOCUMENT
        ZipCode = 12345
        CityName = My City
        Street = My Street 123

## Conversion from IData to POJO

Every writable (`public` or via Setter) attribute of the POJO gets set from IData. The attribute's name determines the corresponding pipeline name read from IData. However, you can also override the pipeline names individually. A simple call to `new IDataConverter().convertToObject(addressIData, Address.class)` would convert the IData above back to the `Address` object with every attribute value set to the value from the pipeline.

# Usage
You need to have some additional libraries on the classpath:

* `[COMMON DIR]/lib/wm-isclient.jar` (from webMethods Integration Server)
* `[COMMON DIR]/lib/ext/enttoolkit.jar` (from webMethods Integration Server)
* `[COMMON DIR]/lib/glassfish/gf.javax.mail.jar`

I have already included them in the project's dependencies (`build.gradle`) if you provide them via your artifact repository (e.g. Artifactory or Nexus).

## Examples

[ao-integrationserver](https://github.com/StefanMacke/ao-integrationserver) uses `ao-idata-converter` to create a framework for calling Integration Server services from Java using type-safe POJOs.
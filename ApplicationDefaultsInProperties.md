# Конфигурационные символы в файле .propetries #

Что-бы вынести конфигурационные символы в файл Cpu.properties достаточно в конфигурацию IOC включить такой код:

```
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        ResourceBundle bundle = ResourceBundle.getBundle("Cpu");
        Enumeration<String> e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            configuration.add(key, bundle.getString(key));
        }
    }
```

теперь в Cpu.properties (в корне classpath) можно включить следующие опции

```
tapestry.supported-locales=en
tapestry.production-mode=false
# The application version number is incorprated into URLs for some
# assets. Web browsers will cache assets because of the far future expires
# header. If existing assets are changed, the version number should also
# change, to force the browser to download new versions.
tapestry.application-version=0.0.2-SNAPSHOT
tapestry.compress-whitespace=${tapestry.production-mode}
```
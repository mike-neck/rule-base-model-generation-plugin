rule-base-model-generation-plugin
===

This is plugin for Gradle, which generates interfaces for rule base model.

Usage
===

### Applying plugin

Copy & paste this snipet into your `build.gradle`.

```groovy
plugins {
    id 'org.mikeneck.rule-based-model-generation' version '0.1'
}
```

### Model definition

You can define your model via `model{}` block.

```groovy
model {
    metaModel { // use metaModel{} block to define you model.
        srcDir = 'src/main/groovy' // default is 'src/main/java'
        packageName = 'sample.gradle.model' // mandatory
        classes.create {
            name = 'Person' // your model class name. classes.name should be Java class name style.
            type = INTERFACE // the type of this class. you can select INTERFACE or ENUM.
            fields.create { // if you select INTERFACE, fields is mandatory.
                name = 'name' // name of field
                type = STRING // the type of field. you can select STRING/INTEGER/LONG/FILE/MODEL_SET...etc.
            }
            fields.create {
                name = 'livingIn' // fields.name should be Java property style.
                type = MODEL_SET
                refType = 'City' // if you defined field as MODEL_SET or MODEL_MAP, refType is mandatory.
            }
            fields.create {
                name = 'sex'
                type = ENUM // also you can select ENUM which refers your model.
                refType = 'Sex' // if you defined property as ENUM or OTHER, refType is mandatory.
            }
        }
        classes.create {
            name = 'Sex'
            type = ENUM
            values.create { // if you select ENUM as classes.type, values is mandatory.
                value = 'WOMEN' // enum value should be constructed with upper case letters and underscore only.
            }
            values.create {
                value = 'MEN'
            }
            values.create {
                value = 'UNKNOWN'
            }
        }
        classes.create {
            name = 'City'
            type = INTERFACE
            fields.create {
                name = 'name'
                type = STRING
            }
            fields.create {
                name = 'country'
                type = STRING
            }
        }
    }
}
```

The `generateModel` task will create 2 interfaces and an enum defined at `src/main/groovy` by this sample `model{}` block.

* interface `sample.gradle.model.Person`
* enum `sample.gradle.model.Sex`
* interface `sample.gradle.model.City`

Tasks
===

* `generateModel` - generates interface defined by `model{}` block.
* `generatePojo` - generates POJO which implements generated interface. This task depends on `generateModel`.

Requirement
===

This plugin is written under these condition...

* Java SE8
* Grdle version 2.6

License
===

Apache 2

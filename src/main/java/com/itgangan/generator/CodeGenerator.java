package com.itgangan.generator;

import freemarker.template.TemplateExceptionHandler;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器，根据数据表名称生成对应的Model、Mapper、Service、Controller简化开发。
 */
public class CodeGenerator {
    //JDBC配置，请修改为你项目的实际配置
    private static final String JDBC_URL = "jdbc:mysql://118.144.88.220:3307/metrology_institute?useUnicode=true&characterEncoding=utf-8";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "kaerbaqian321";
    private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/main/resources/generator/template";//模板位置

    private static final String JAVA_PATH = "/src/main/java"; //java文件路径
    private static final String RESOURCES_PATH = "/src/main/resources";//资源文件路径

    private static final String PACKAGE_PATH_SERVICE = packageConvertPath(ProjectConstant.SERVICE_PACKAGE);//生成的Service存放路径
    private static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(ProjectConstant.SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    private static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(ProjectConstant.CONTROLLER_PACKAGE);//生成的Controller存放路径

    private static final String AUTHOR = "ganxiangyong";//@author
    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date

    public static void main(String[] args) {
        CodeGenerator generator = new CodeGenerator();
        generator.genCode("DepartmentInfo");
    }

    public void genCode(String... tableNames) {
        for (String tableName : tableNames) {
            //根据需求生成，不需要的注掉，模板有问题的话可以自己修改。
            genModelAndMapper(tableName);
            genService(tableName);
            genController(tableName);
        }
    }

    public void genModelAndMapper(String tableName) {
        Context context = createContext();

        context.setJdbcConnectionConfiguration(createJDBCConnectionConfiguration());

        context.setJavaModelGeneratorConfiguration(createJavaModelGeneratorConfiguration());

        context.setSqlMapGeneratorConfiguration(createSqlMapGeneratorConfiguration());

        context.addPluginConfiguration(createPluginConfiguration());

        context.setJavaClientGeneratorConfiguration(createJavaClientGeneratorConfiguration());

        context.addTableConfiguration(createTableConfiguration(context, tableName));

        List<String> warnings = new ArrayList<>();;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }

        String modelName = tableNameConvertUpperCamel(tableName);

        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");
    }

    private Context createContext(){
        Context context = new Context(ModelType.FLAT);
        context.setId("contextId");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING, "UTF-8");
        return context;
    }

    private JDBCConnectionConfiguration createJDBCConnectionConfiguration(){
        JDBCConnectionConfiguration config = new JDBCConnectionConfiguration();
        config.setConnectionURL(JDBC_URL);
        config.setUserId(JDBC_USERNAME);
        config.setPassword(JDBC_PASSWORD);
        config.setDriverClass(JDBC_DIVER_CLASS_NAME);
        return config;
    }

    private PluginConfiguration createPluginConfiguration(){
        PluginConfiguration config = new PluginConfiguration();
        config.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        config.addProperty("mappers", ProjectConstant.MAPPER_INTERFACE_REFERENCE);
        return config;
    }

    private JavaModelGeneratorConfiguration createJavaModelGeneratorConfiguration(){
        JavaModelGeneratorConfiguration config = new JavaModelGeneratorConfiguration();
        config.setTargetProject(PROJECT_PATH + JAVA_PATH);
        config.setTargetPackage(ProjectConstant.MODEL_PACKAGE);
        return config;
    }

    private SqlMapGeneratorConfiguration createSqlMapGeneratorConfiguration(){
        SqlMapGeneratorConfiguration config = new SqlMapGeneratorConfiguration();
        config.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        config.setTargetPackage("mapper");
        return config;
    }

    private JavaClientGeneratorConfiguration createJavaClientGeneratorConfiguration(){
        JavaClientGeneratorConfiguration config = new JavaClientGeneratorConfiguration();
        config.setTargetProject(PROJECT_PATH + JAVA_PATH);
        config.setTargetPackage(ProjectConstant.MAPPER_PACKAGE);
        config.setConfigurationType("XMLMAPPER");
        return config;
    }

    private TableConfiguration createTableConfiguration(Context context, String tableName){
        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "MySql", true, null));
        return tableConfiguration;
    }

    public static void genService(String tableName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = tableNameConvertUpperCamel(tableName);
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", ProjectConstant.BASE_PACKAGE);

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    public static void genController(String tableName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", AUTHOR);
            data.put("baseRequestMapping", tableNameConvertMappingPath(tableName));
            String modelNameUpperCamel = tableNameConvertUpperCamel(tableName);
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", ProjectConstant.BASE_PACKAGE);

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration();
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        StringBuilder result = new StringBuilder();
        if (tableName != null && tableName.length() > 0) {
            tableName = tableName.toLowerCase();//兼容使用大写的表名
            boolean flag = false;
            for (int i = 0; i < tableName.length(); i++) {
                char ch = tableName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        String camel = tableNameConvertLowerCamel(tableName);
        return camel.substring(0, 1).toUpperCase() + camel.substring(1);

    }

    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();//兼容使用大写的表名
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }
}

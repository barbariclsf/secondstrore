package com.squirrel;
import org.apache.ibatis.io.Resources;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class mybatisTest {
    public static void main(String[] args) throws  Exception{
        //MBG执行过程中的警告信息
        List<String> warings = new ArrayList<String>();
        //当生成的代码重复时，覆盖源代码
        boolean override = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warings);
        Configuration config = cp.parseConfiguration(configFile);

        DefaultShellCallback callback = new DefaultShellCallback(override);
        //创建BGM
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback,warings);
        myBatisGenerator.generate(null);

        for (String war:warings){
            System.out.println(war);
        }

    }
}

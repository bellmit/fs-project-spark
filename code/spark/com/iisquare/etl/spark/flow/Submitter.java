package com.iisquare.etl.spark.flow;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.deploy.SparkSubmit;

import com.iisquare.etl.spark.config.Configuration;
import com.iisquare.jwframe.service.FlowService;
import com.iisquare.jwframe.utils.DPUtil;

public class Submitter {

	/**
	 * 参照：http://spark.apache.org/docs/latest/submitting-applications.html
	 */
	public static boolean submit(String json, boolean forceReload) throws Exception {
		Configuration config = Configuration.getInstance();
		String master = config.getProperty("master", "local");
		String appName = config.getProperty("app.name", "etl-visual");
		if(master.matches("^local(\\[\\w+\\])?$")) {
			System.setProperty("spark.master", master);
			System.setProperty("spark.app.name", appName);
			TaskRunner.main(new String[]{json});
		} else {
			FlowService flowService = new FlowService();
			List<String> jarList = flowService.generateJars(forceReload);
			List<String> argList = new ArrayList<>();
			argList.add("--master");
			argList.add(master);
			argList.add("--name");
			argList.add(appName);
			argList.add("--deploy-mode");
			argList.add(config.getProperty("deploy.mode", "client"));
			argList.add("--class");
			argList.add(TaskRunner.class.getName());
			if(!jarList.isEmpty()) {
				argList.add("--jars");
				argList.add(DPUtil.implode(",", DPUtil.collectionToArray(jarList)));
			}
			argList.add("build/libs/etl-visual.jar");
			argList.add(json);
			System.out.println(argList);
			SparkSubmit.main(DPUtil.collectionToStringArray(argList));
		}
		return true;
	}

}

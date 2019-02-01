package org.haitham.demoswagger;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class TestExecutor {

    public static void main(String args[]) {
        new TestExecutor("~@ignore");
    }


    /**
     * Constructor
     *
     * @param tags DBB_TAG_SCOPE
     */
    TestExecutor(String tags) {
        this.executor(tags);
    }

    /**
     * PRIVATE test executor
     *
     * @param tags BDD_TAG_SCOPE
     */
    private void executor(String tags) {
        if (tags.isEmpty()) {
            tags = "~@ignore";
        }
        try {
            for (String featureFile : getResourceFolderFiles()) {
                ExportResource("/" + featureFile,
                        new File(featureFile)
                );
            }
            ClassLoader classLoader = TestExecutor.class.getClassLoader();
            ResourceLoader resourceLoader = new MultiLoader(classLoader);
            ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
            RuntimeOptions ro = new RuntimeOptions(Common.getCucumberPlugins());
            List<String> listTags;
            listTags = Arrays.asList(tags.split("\\s*;\\s*"));
            if (listTags.isEmpty()) {
                listTags.add(tags);
            }
            for (String tag : listTags) {
                ro.getFilters().add(tag);
            }
            for (String feature : Common.getCucumberFeaturePath()) {
                createCucumberFeatureDirectoryIifDoesNotExist(feature);
                ro.getFeaturePaths().add(feature);
            }
            for (String glue : Common.getCucumberGlue()) {
                ro.getGlue().add(glue);
            }
            cucumber.runtime.Runtime runtime = new cucumber.runtime.Runtime(
                    resourceLoader, classFinder, classLoader, ro
            );
            runtime.run();
        } catch (Exception e) {
            System.out.println("Problem at cucumber executor: " + e.getMessage());
        }
    }


    /**
     * PRIVATE export resources from jar file
     *
     * @param sResource resource file into jar file
     * @param fDest     exported file location
     * @throws Exception General exception
     */
    private void ExportResource(String sResource, File fDest) throws Exception {
        if (sResource == null || fDest == null) {
            throw new Exception("Can not generate locally file(s)");
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (!fDest.getParentFile().mkdirs()) {
                throw new Exception("Problem to create directory");
            }
            new File(sResource);
        } catch (Exception e) {
            System.out.println("Ignored: Problem at generating locally feature file(s). Detail: " + e);
        }
        try {
            int nLen;
            inputStream = TestExecutor.class.getResourceAsStream(sResource);
            if (inputStream == null)
                throw new IOException("Error copying from jar");
            outputStream = new FileOutputStream(fDest);
            byte[] bBuffer = new byte[1024];
            while ((nLen = inputStream.read(bBuffer)) > 0)
                outputStream.write(bBuffer, 0, nLen);
            outputStream.flush();
        } catch (IOException ex) {
            System.out.println("Problem export resources. Detail: " + ex);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException eError) {
                System.out.println("Problem export resources. Detail: " + eError);
            }
        }
    }

    /**
     * PRIVATE getter of resource folder file(s)
     *
     * @return List of files location
     */
    private List<String> getResourceFolderFiles() throws Exception {
        List<String> featuresFiles = new ArrayList<>();
        CodeSource src = TestExecutor.class.getProtectionDomain().getCodeSource();
        if (src == null) {
            System.out.println("No feature files");
            return featuresFiles;
        }

        URL jar = src.getLocation();
        ZipInputStream zip;
        try {
            zip = new ZipInputStream(jar.openStream());
            ZipEntry zipEntry = zip.getNextEntry();
            while (zipEntry != null) {
                String name = zipEntry.getName();
                for (String featurePath : Common.getCucumberFeaturePath()) {
                    if ((name.startsWith(featurePath)) && (name.endsWith(".feature"))) {
                        featuresFiles.add(name);
                        System.out.println("Feature file: " + name);
                    }
                }
                zipEntry = zip.getNextEntry();
            }
            zip.close();
        } catch (IOException e) {
            System.out.println("Problem to create input stream from ZIP file. Detail: " + e);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Problem to read from ZIP file. (It can be only end of file) Detail: " + nullPointerException);
        } catch (Exception zipEntryException) {
            System.out.println("Problem to read from ZIP file. Detail: " + zipEntryException);
        }
        return featuresFiles;
    }

    /**
     * PRIVATE method for creating Cucumber feature directory if not exists. Cucumber runner without specific feature
     * directory is failing
     *
     * @param featureDirectory Cucumber feature directory
     */
    private void createCucumberFeatureDirectoryIifDoesNotExist(String featureDirectory) {
        try {
            createDirectoryIfDoesNotExist(featureDirectory);
        } catch (Exception e) {
            System.out.println("Create missing Cucumber directory " + featureDirectory + " has failed. Detail: " + e);
        }
    }

    private static Path createDirectoryIfDoesNotExist(String dir) throws Exception {
        Path directory = null;
        Path absoluteDirectory = Paths.get(dir);
        if (absoluteDirectory.isAbsolute()) {
            if (Files.isDirectory(absoluteDirectory)) {
                if (!Files.exists(absoluteDirectory)) {
                    try {
                        Files.createDirectories(absoluteDirectory);
                    } catch (IOException e) {
                        System.out.println(
                                "Problem to create directory (absolute): " + String.valueOf(absoluteDirectory)
                                        + " Detail: " + e
                        );
                        throw e;
                    }
                }
                directory = absoluteDirectory;
            }
        } else {
            Path relativeDirectory = Paths.get(String.valueOf(Paths.get(".")), dir);
            if (!Files.exists(relativeDirectory)) {
                try {
                    Files.createDirectories(relativeDirectory);
                } catch (IOException e) {
                    System.out.println(
                            "Problem to create directory (relative): " + String.valueOf(absoluteDirectory)
                                    + " Detail: " + e
                    );
                    throw e;
                }
            }
            directory = relativeDirectory;
        }
        if (directory != null) {
            System.out.println("Directory " + directory.toAbsolutePath().toString() + " is ready");
            return directory;
        }
        String msg = "Problem to create directory " + dir + " Detail: after creating directory path is still null";
        System.out.println(msg);
        throw new Exception(msg);
    }
}

package com.tvd.gameview.ext.file;

//Ahy - A pure java CMS.
//Copyright (C) 2010 Sidney Leal (manish.com.br)
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

//package br.com.manish.ahy.kernel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class FileUtil {

	public static void copyFile(String from, String to) {
	    copyFile(from, to, Boolean.FALSE);
	}

	public static void copyFile(String from, String to, Boolean overwrite) {

	    try {
	        File fromFile = new File(from);
	        File toFile = new File(to);
	
	        if (!fromFile.exists()) {
	            throw new IOException("File not found: " + from);
	        }
	        if (!fromFile.isFile()) {
	            throw new IOException("Can't copy directories: " + from);
	        }
	        if (!fromFile.canRead()) {
	            throw new IOException("Can't read file: " + from);
	        }
	
	        if (toFile.isDirectory()) {
	            toFile = new File(toFile, fromFile.getName());
	        }
	
	        if (toFile.exists() && !overwrite) {
	            throw new IOException("File already exists.");
	        } else {
	            String parent = toFile.getParent();
	            if (parent == null) {
	                parent = System.getProperty("user.dir");
	            }
	            File dir = new File(parent);
	            if (!dir.exists()) {
	                throw new IOException("Destination directory does not exist: " + parent);
	            }
	            if (dir.isFile()) {
	                throw new IOException("Destination is not a valid directory: " + parent);
	            }
	            if (!dir.canWrite()) {
	                throw new IOException("Can't write on destination: " + parent);
	            }
	        }
	
	        FileInputStream fis = null;
	        FileOutputStream fos = null;
	        try {
	
	            fis = new FileInputStream(fromFile);
	            fos = new FileOutputStream(toFile);
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	
	            while ((bytesRead = fis.read(buffer)) != -1) {
	                fos.write(buffer, 0, bytesRead);
	            }
	
	        } finally {
	            if (from != null) {
	                try {
	                    fis.close();
	                } catch (IOException e) {
	                  System.out.println(e);
	                }
	            }
	            if (to != null) {
	                try {
	                    fos.close();
	                } catch (IOException e) {
	                    System.out.println(e);
	                }
	            }
	        }
	
	    } catch (IOException e) {
	        System.out.println("Problems when copying file.");
	    }
	}

}
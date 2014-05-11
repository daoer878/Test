/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

/**
 * Packaged jar assets root
 * Any assets under this folder can be accessed by:
 * new CosmicResource("assets/myfolder/myasset.ass").getStream() or getURL()
 * When the application initializes its managers use this line to set the 
 * packaged assets jar folder:
 * CosmicResource.loaderClass = JarLoader.class;
 * @author Lou Hayt
 */
public class JarLoader {}
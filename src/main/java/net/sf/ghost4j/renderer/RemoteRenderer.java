/*
 * Ghost4J: a Java wrapper for Ghostscript API.
 *
 * Distributable under LGPL license.
 * See terms of license at http://www.gnu.org/licenses/lgpl.html.
 */
package net.sf.ghost4j.renderer;

import java.awt.Image;
import java.io.IOException;
import java.util.List;

import net.sf.ghost4j.analyzer.AnalyzerException;
import net.sf.ghost4j.document.Document;
import net.sf.ghost4j.document.DocumentException;

/**
 * Interface defining a remote renderer (for Ghostscript multi process support).
 * @author Gilles Grousset (gi.grousset@gmail.com)
 */
public interface RemoteRenderer extends Renderer {

	/**
     * Sets max parallel rendering processes allowed for the renderer
     * @param maxProcessCount
     */
    public void setMaxProcessCount(int maxProcessCount);

    /**
	 * Renders pages of a given document an outputs result as a list of Image objects (on image per page).
	 * @param document Document to render. Document type may or may no be supported (support left to the render final implementation).
	 * @param begin Index of the first page to render
	 * @param end Index of the last page to render
	 * @return a List of Image objects
	 * @throws IOException
	 * @throws AnalyzerException
	 * @throws RendererException
	 */
	public List<Image> remoteRender(Document document, int begin, int end) throws IOException, RendererException, DocumentException;
	
	/**
     * Clones settings of a renderer to another one.
     * This operation is allowed only when renderers (source and target) are instances of the same class.
     * @param remoteRenderer
     */
    public void cloneSettings(RemoteRenderer remoteRenderer);
}
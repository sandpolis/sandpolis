/*******************************************************************************
 *                                                                             *
 *                Copyright © 2015 - 2019 Subterranean Security                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *                                                                             *
 ******************************************************************************/
package com.sandpolis.viewer.jfx.view.about;

import static javafx.scene.shape.VertexFormat.POINT_NORMAL_TEXCOORD;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.LittleEndianDataInputStream;

import javafx.scene.shape.TriangleMesh;

/**
 * A small parser for binary STL models.
 *
 * @author cilki
 * @since 5.0.1
 */
public final class StlParser {

	/**
	 * Parse a STL model from the given stream.
	 *
	 * @param stl The STL stream
	 * @return A new {@link TriangleMesh} representing the model
	 * @throws IOException If an error occurs while reading from the stream
	 */
	public static TriangleMesh parse(InputStream stl) throws IOException {
		TriangleMesh mesh = new TriangleMesh(POINT_NORMAL_TEXCOORD);

		// Texture coordinates not supported
		mesh.getTexCoords().addAll(0, 0);

		try (var in = new LittleEndianDataInputStream(new BufferedInputStream(stl))) {
			// Skip 80-byte header
			in.skipBytes(80);

			// Warn the mesh about the number of triangles incoming
			int triangles = in.readInt();
			mesh.getNormals().ensureCapacity(triangles * 3);
			mesh.getPoints().ensureCapacity(triangles * 9);
			mesh.getFaces().ensureCapacity(triangles * 9);

			int normals = 0;
			int points = 0;

			while (in.available() > 0) {

				// Read normal vector
				mesh.getNormals().addAll(in.readFloat(), in.readFloat(), in.readFloat());
				normals += 1;

				// Read the triangle's three verticies
				mesh.getPoints().addAll(in.readFloat(), in.readFloat(), in.readFloat());
				mesh.getPoints().addAll(in.readFloat(), in.readFloat(), in.readFloat());
				mesh.getPoints().addAll(in.readFloat(), in.readFloat(), in.readFloat());
				points += 3;

				// Link the last three verticies together as a face
				mesh.getFaces().addAll(points - 3, normals - 1, 0);
				mesh.getFaces().addAll(points - 2, normals - 1, 0);
				mesh.getFaces().addAll(points - 1, normals - 1, 0);

				// Skip attribute field
				in.readShort();
			}
		}

		return mesh;
	}
}

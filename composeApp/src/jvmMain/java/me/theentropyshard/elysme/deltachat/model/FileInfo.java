/*
 * Elysme - https://github.com/TheEntropyShard/Elysme
 * Copyright (C) 2026 TheEntropyShard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.elysme.deltachat.model;

public class FileInfo {
    private final String file;
    private final long fileBytes;
    private final String fileMime;
    private final String fileName;
    private final int width;
    private final int height;

    public FileInfo(String file, long fileBytes, String fileMime, String fileName, int width, int height) {
        this.file = file;
        this.fileBytes = fileBytes;
        this.fileMime = fileMime;
        this.fileName = fileName;
        this.width = width;
        this.height = height;
    }

    public String getFile() {
        return this.file;
    }

    public long getFileBytes() {
        return this.fileBytes;
    }

    public String getFileMime() {
        return this.fileMime;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

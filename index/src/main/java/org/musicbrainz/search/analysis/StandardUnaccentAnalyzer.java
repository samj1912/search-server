/* Copyright (c) 2008 Lukas Lalinsky
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the MusicBrainz project nor the names of the
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.musicbrainz.search.analysis;

import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;

/**
 * Filters StandardTokenizer with StandardFilter, AccentFilter, LowerCaseFilter
 * and StopFilter, using a list of English stop words.
 */
public class StandardUnaccentAnalyzer extends Analyzer {

    public TokenStream tokenStream(String fieldName, Reader reader) {
        StandardTokenizer tokenStream = new StandardTokenizer(reader);
        TokenStream result = new StandardFilter(tokenStream);
        result = new AccentFilter(result);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS);
        return result;
    }
    
    private static final class SavedStreams {
        StandardTokenizer tokenStream;
        TokenStream filteredTokenStream;
    }
    
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        SavedStreams streams = (SavedStreams)getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            setPreviousTokenStream(streams);
            streams.tokenStream = new StandardTokenizer(reader);
            streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
            streams.filteredTokenStream = new AccentFilter(streams.filteredTokenStream);
            streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
            streams.filteredTokenStream = new StopFilter(streams.filteredTokenStream, StopAnalyzer.ENGLISH_STOP_WORDS);
        }
        else {
            streams.tokenStream.reset(reader);
        }
        return streams.filteredTokenStream;
    }

}

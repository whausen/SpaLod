/*
 * Copyright (C) 2021 Dr. Jean-Jacques Ponciano.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package info.ponciano.lab.spalodwfs.mvc.models;

public class SparqlQuery {

	private String query;
    private String triplestore;


    public SparqlQuery(String query, String triplestore) {
        this.query = query;
        this.triplestore = triplestore;
    }

    public SparqlQuery() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResults() {
        return query;
    }

    public void setResults(String query) {
        this.query = query;
    }

    public void setTriplestore(String triplestore) {
        this.triplestore = triplestore;
    }

    public String getTriplestore() {
        return triplestore;
    }

    @Override
    public String toString() {
        return "SparqlQuery{" +
                "query='" + query + '\'' +
                ", triplestore='" + triplestore + '\'' +
                '}';
    }
}


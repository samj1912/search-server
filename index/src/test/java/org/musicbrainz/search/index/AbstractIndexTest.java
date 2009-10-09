package org.musicbrainz.search.index;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.BatchUpdateException;


public abstract class AbstractIndexTest extends TestCase {

    protected Connection createConnection() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:mem");

    }

    public void setup() throws Exception {
        try {
            Connection conn = createConnection();
            conn.setAutoCommit(true);

            //Drop tables, if they exist
            try {
                Statement stmt = conn.createStatement();
                stmt.addBatch("DROP TABLE country");
                stmt.addBatch("DROP TABLE language");
                stmt.addBatch("DROP TABLE script");

                stmt.addBatch("DROP TABLE artist");
                stmt.addBatch("DROP TABLE artist_alias");
                stmt.addBatch("DROP TABLE artist_name");
                stmt.addBatch("DROP TABLE artist_type");
                stmt.addBatch("DROP TABLE artist_credit");
                stmt.addBatch("DROP TABLE artist_credit_name");
                stmt.addBatch("DROP TABLE gender");

                stmt.addBatch("DROP TABLE label");
                stmt.addBatch("DROP TABLE label_alias");
                stmt.addBatch("DROP TABLE label_name");
                stmt.addBatch("DROP TABLE label_type");

                stmt.addBatch("DROP TABLE release");
                stmt.addBatch("DROP TABLE release_name");
                stmt.addBatch("DROP TABLE release_status");
                stmt.addBatch("DROP TABLE release_packaging");
                stmt.addBatch("DROP TABLE release_label");
                stmt.addBatch("DROP TABLE medium");
                stmt.addBatch("DROP TABLE medium_format");
                stmt.addBatch("DROP TABLE medium_cdtoc");
                stmt.addBatch("DROP TABLE release_meta");
                stmt.addBatch("DROP TABLE tracklist");


                stmt.addBatch("DROP TABLE release_group");
                stmt.addBatch("DROP TABLE release_group_type");

                stmt.addBatch("DROP TABLE track_name");
                stmt.addBatch("DROP TABLE track");
                stmt.addBatch("DROP TABLE recording");

                stmt.addBatch("DROP TABLE annotation");
                stmt.addBatch("DROP TABLE artist_annotation");
                stmt.addBatch("DROP TABLE label_annotation");
                stmt.addBatch("DROP TABLE recording_annotation");
                stmt.addBatch("DROP TABLE release_annotation");
                stmt.addBatch("DROP TABLE release_group_annotation");
                stmt.addBatch("DROP TABLE work_annotation");

                stmt.executeBatch();
                stmt.close();
            }
            catch (BatchUpdateException bue) {
                //Ignore errors, because will just be that tables does not exist
                System.err.println(bue.getMessage());
            }

            //Create tables and data for Artist Index
            Statement stmt = conn.createStatement();

            setupCommonTables(stmt);
            setupArtistTables(stmt);
            setupLabelTables(stmt);
            setupReleaseTables(stmt);
            setupReleaseGroupTables(stmt);
            setupRecordingTables(stmt);
            setupAnnotationTables(stmt);
            stmt.executeBatch();
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    protected void setupCommonTables(Statement stmt) throws Exception {
        stmt.addBatch("CREATE TABLE country" +
                "(" +
                "  id serial NOT NULL," +
                "  isocode character varying(2) NOT NULL," +
                "  name character varying(100) NOT NULL" +
                ")");

        stmt.addBatch("CREATE TABLE language" +
                "(" +
                "  id serial NOT NULL," +
                "  isocode_3t character(3) NOT NULL," +
                "  isocode_3b character(3) NOT NULL," +
                "  isocode_2 character(2)," +
                "  name character varying(100) NOT NULL," +
                "  frequency integer NOT NULL DEFAULT 0," +
                "  CONSTRAINT language_pkey PRIMARY KEY (id)" +
                ")");

        stmt.addBatch("CREATE TABLE script" +
                "(" +
                "  id serial NOT NULL," +
                "  isocode character(4) NOT NULL," +
                "  isonumber character(3) NOT NULL," +
                "  name character varying(100) NOT NULL," +
                "  frequency integer NOT NULL DEFAULT 0" +
                ")");
    }

    protected void setupArtistTables(Statement stmt) throws Exception {

        stmt.addBatch("CREATE TABLE artist (" +
                "  id serial NOT NULL," +
                "  gid character(36) NOT NULL," +
                "  name integer NOT NULL," +
                "  sortname integer NOT NULL," +
                "  begindate_year integer," +
                "  begindate_month integer," +
                "  begindate_day integer," +
                "  enddate_year integer," +
                "  enddate_month integer," +
                "  enddate_day integer," +
                "  type integer," +
                "  country integer," +
                "  gender integer," +
                "  comment character varying(255)," +
                "  editpending integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE artist_credit (" +
                "  id serial NOT NULL," +
                "  artistcount integer NOT NULL," +
                "  refcount integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE artist_credit_name (" +
                "  artist_credit integer NOT NULL," +
                "  position integer NOT NULL," +
                "  artist integer NOT NULL," +
                "  name integer NOT NULL," +
                "  joinphrase character varying(32)" +
                ")");

        stmt.addBatch("CREATE TABLE artist_alias (" +
                "  id serial NOT NULL," +
                "  artist integer NOT NULL," +
                "  name integer NOT NULL," +
                "  editpending integer NOT NULL DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE artist_name (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL," +
                "  refcount integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE artist_type (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");

        stmt.addBatch("CREATE TABLE gender (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");
    }

    protected void setupLabelTables(Statement stmt) throws Exception {
        stmt.addBatch("CREATE TABLE label (" +
                "  id serial NOT NULL," +
                "  gid character(36) NOT NULL," +
                "  name integer NOT NULL," +
                "  sortname integer NOT NULL," +
                "  begindate_year integer," +
                "  begindate_month integer," +
                "  begindate_day integer," +
                "  enddate_year integer," +
                "  enddate_month integer," +
                "  enddate_day integer," +
                "  labelcode integer," +
                "  type integer," +
                "  country integer," +
                "  comment character varying(255)," +
                "  editpending integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE label_alias (" +
                "  id serial NOT NULL," +
                "  label integer NOT NULL," +
                "  name integer NOT NULL," +
                "  editpending integer NOT NULL DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE label_name (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL," +
                "  refcount integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE label_type (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");
    }

    protected void setupReleaseTables(Statement stmt) throws Exception {

        stmt.addBatch("CREATE TABLE release (" +
                "  id serial NOT NULL," +
                "  gid character(36)," +
                "  name integer NOT NULL," +
                "  artist_credit integer NOT NULL," +
                "  release_group integer NOT NULL," +
                "  status integer," +
                "  packaging integer," +
                "  country integer," +
                "  language integer," +
                "  script integer," +
                "  date_year integer," +
                "  date_month integer," +
                "  date_day integer," +
                "  barcode character varying(255)," +
                "  comment character varying(255)," +
                "  editpending integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE release_name (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL," +
                "  refcount integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE release_status (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");

        stmt.addBatch("CREATE TABLE release_packaging (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");

        stmt.addBatch("CREATE TABLE release_label (" +
                "  id serial NOT NULL," +
                "  release integer NOT NULL," +
                "  label integer," +
                "  catno character varying(255)" +
                ")");

        stmt.addBatch("CREATE TABLE medium (" +
                "  id serial NOT NULL," +
                "  tracklist integer NOT NULL," +
                "  release integer NOT NULL," +
                "  position integer NOT NULL," +
                "  format integer," +
                "  name character varying(255)," +
                "  editpending integer DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE medium_format (" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL," +
                "  year integer" +
                ")");

        stmt.addBatch("CREATE TABLE release_meta" +
                "(" +
                "  id integer NOT NULL," +
                "  lastupdate timestamp," +
                "  dateadded timestamp," +
                "  coverarturl character varying(255)," +
                "  infourl character varying(255)," +
                "  amazonasin character varying(10)," +
                "  amazonstore character varying(20)" +
                ")");

        stmt.addBatch("CREATE TABLE medium_cdtoc" +
                "(" +
                "  id serial NOT NULL," +
                "  medium integer NOT NULL," +
                "  cdtoc integer NOT NULL," +
                "  editpending integer NOT NULL DEFAULT 0," +
                "  CONSTRAINT medium_cdtoc_pkey PRIMARY KEY (id)" +
                ")");

        stmt.addBatch("CREATE TABLE tracklist" +
                "(" +
                "  id serial NOT NULL," +
                "  trackcount integer NOT NULL DEFAULT 0," +
                "  CONSTRAINT tracklist_pkey PRIMARY KEY (id)" +
                ")");
    }

    protected void setupReleaseGroupTables(Statement stmt) throws Exception {

        stmt.addBatch("CREATE TABLE release_group" +
                "(" +
                "  id serial NOT NULL," +
                "  gid uuid NOT NULL," +
                "  name integer NOT NULL," +
                "  artist_credit integer NOT NULL," +
                "  type integer," +
                "  comment character varying(255)," +
                "  editpending integer NOT NULL DEFAULT 0" +
                ")");

        stmt.addBatch("CREATE TABLE release_group_type" +
                "(" +
                "  id serial NOT NULL," +
                "  name character varying(255) NOT NULL" +
                ")");
    }

    protected void setupRecordingTables(Statement stmt) throws Exception {

        stmt.addBatch("CREATE TABLE track_name" +
                "(" +
                "  id serial NOT NULL," +
                "  name character varying NOT NULL," +
                "  refcount integer DEFAULT 0," +
                "  CONSTRAINT track_name_pkey PRIMARY KEY (id)" +
                ")");

        stmt.addBatch("CREATE TABLE track" +
                "(" +
                "  id serial NOT NULL," +
                "  recording integer NOT NULL," +
                "  tracklist integer NOT NULL," +
                "  position integer NOT NULL," +
                "  name integer NOT NULL," +
                "  artist_credit integer NOT NULL," +
                "  length integer," +
                "  editpending integer NOT NULL DEFAULT 0," +
                "  CONSTRAINT track_pkey PRIMARY KEY (id)" +
                ")");

        stmt.addBatch("CREATE TABLE recording" +
                "(" +
                "  id serial NOT NULL," +
                "  gid uuid NOT NULL," +
                "  name integer NOT NULL," +
                "  artist_credit integer NOT NULL," +
                "  length integer," +
                "  comment character varying(255)," +
                "  editpending integer NOT NULL DEFAULT 0," +
                "  CONSTRAINT recording_pkey PRIMARY KEY (id)" +
                ")");
    }

    protected void setupAnnotationTables(Statement stmt) throws Exception {
        stmt.addBatch(
                "CREATE TABLE annotation" +
                "(" +
                "  id serial NOT NULL, " +
                "  editor integer NOT NULL," +
                "  text text," +
                "  changelog character varying(255), " +
                "  created timestamp," +
                "  CONSTRAINT annotation_pkey PRIMARY KEY (id))" );


        stmt.addBatch("CREATE TABLE artist_annotation" +
                "(" +
                "  artist integer NOT NULL," +
                "  annotation integer NOT NULL," +
                "  CONSTRAINT artist_annotation_pkey PRIMARY KEY (artist, annotation)" +
                ")");
        
        stmt.addBatch("CREATE TABLE label_annotation" +
                "(" +
                "  label integer NOT NULL," +
                "  annotation integer NOT NULL," +
                "  CONSTRAINT label_annotation_pkey PRIMARY KEY (label, annotation)" +
                ")");
        
        stmt.addBatch("CREATE TABLE recording_annotation" +
                "(" +
                "  recording integer NOT NULL," +
                "  annotation integer NOT NULL," +
                "  CONSTRAINT recording_annotation_pkey PRIMARY KEY (recording, annotation)" +
                ")");

        stmt.addBatch("CREATE TABLE release_annotation" +
                        "(" +
                        "  release integer NOT NULL," +
                        "  annotation integer NOT NULL," +
                        "  CONSTRAINT release_annotation_pkey PRIMARY KEY (release, annotation)" +
                        ")");

        stmt.addBatch("CREATE TABLE release_group_annotation" +
                "(" +
                "  release_group integer NOT NULL," +
                "  annotation integer NOT NULL," +
                "  CONSTRAINT release_group_annotation_pkey PRIMARY KEY (release_group, annotation)" +
                ")");
        
        stmt.addBatch("CREATE TABLE work_annotation" +
                "(" +
                "  work integer NOT NULL," +
                "  annotation integer NOT NULL," +
                "  CONSTRAINT work_annotation_pkey PRIMARY KEY (work, annotation)" +
                ")");
                                        

    }


}

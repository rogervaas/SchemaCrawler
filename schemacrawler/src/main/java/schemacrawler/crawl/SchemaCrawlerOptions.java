/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2007, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.crawl;


import java.util.Arrays;
import java.util.Properties;

import schemacrawler.main.BaseOptions;
import schemacrawler.schema.TableType;
import schemacrawler.util.AlphabeticalSortComparator;
import schemacrawler.util.NaturalSortComparator;
import schemacrawler.util.SerializableComparator;

/**
 * SchemaCrarlwe options.
 * 
 * @author sfatehi
 */
public final class SchemaCrawlerOptions
  extends BaseOptions
{

  private static final String DEFAULT_TABLE_TYPES = "TABLE,VIEW";

  private static final long serialVersionUID = -3557794862382066029L;

  private static final String SC_SHOW_STORED_PROCEDURES = "schemacrawler.show_stored_procedures";
  private static final String SC_COLUMN_PATTERN_EXCLUDE = "schemacrawler.column.pattern.exclude";
  private static final String SC_COLUMN_PATTERN_INCLUDE = "schemacrawler.column.pattern.include";
  private static final String SC_TABLE_PATTERN_EXCLUDE = "schemacrawler.table.pattern.exclude";
  private static final String SC_TABLE_PATTERN_INCLUDE = "schemacrawler.table.pattern.include";
  private static final String SC_SORT_ALPHABETICALLY_PROCEDURE_COLUMNS = "schemacrawler.sort_alphabetically.procedure_columns";
  private static final String SC_SORT_ALPHABETICALLY_TABLE_INDICES = "schemacrawler.sort_alphabetically.table_indices";
  private static final String SC_SORT_ALPHABETICALLY_TABLE_FOREIGNKEYS = "schemacrawler.sort_alphabetically.table_foreignkeys";
  private static final String SC_SORT_ALPHABETICALLY_TABLE_COLUMNS = "schemacrawler.sort_alphabetically.table_columns";
  private static final String SC_TABLE_TYPES = "schemacrawler.table_types";

  private static TableType[] copyTableTypes(final TableType[] tableTypes)
  {
    final TableType[] tableTypesCopy = new TableType[tableTypes.length];
    System.arraycopy(tableTypes, 0, tableTypesCopy, 0, tableTypes.length);
    return tableTypesCopy;
  }

  private TableType[] tableTypes;

  private boolean showStoredProcedures;

  private InclusionRule tableInclusionRule;
  private InclusionRule columnInclusionRule;

  private SerializableComparator tableColumnComparator;
  private SerializableComparator tableForeignKeyComparator;
  private SerializableComparator tableIndexComparator;

  private SerializableComparator procedureColumnComparator;

  /**
   * Default options.
   */
  public SchemaCrawlerOptions()
  {
    tableTypes = TableType.valueOf(DEFAULT_TABLE_TYPES.split(","));

    showStoredProcedures = false;

    tableInclusionRule = new InclusionRule();
    columnInclusionRule = new InclusionRule();

    tableColumnComparator = new NaturalSortComparator();
    tableForeignKeyComparator = new NaturalSortComparator();
    tableIndexComparator = new NaturalSortComparator();
    procedureColumnComparator = new NaturalSortComparator();

  }

  /**
   * Options from properties.
   * 
   * @param config
   *        Configuration properties
   */
  public SchemaCrawlerOptions(final Properties config)
  {

    final String tableTypesString = config.getProperty(SC_TABLE_TYPES,
                                                       DEFAULT_TABLE_TYPES);
    tableTypes = TableType.valueOf(tableTypesString.split(","));

    showStoredProcedures = getBooleanProperty(SC_SHOW_STORED_PROCEDURES, config);

    tableInclusionRule = new InclusionRule(config
      .getProperty(SC_TABLE_PATTERN_INCLUDE, ".*"), config
      .getProperty(SC_TABLE_PATTERN_EXCLUDE, ".*"));
    columnInclusionRule = new InclusionRule(config
      .getProperty(SC_COLUMN_PATTERN_INCLUDE, ".*"), config
      .getProperty(SC_COLUMN_PATTERN_EXCLUDE, ".*"));

    // comparators
    tableColumnComparator = getComparator(SC_SORT_ALPHABETICALLY_TABLE_COLUMNS,
                                          config);
    tableForeignKeyComparator = getComparator(SC_SORT_ALPHABETICALLY_TABLE_FOREIGNKEYS,
                                              config);
    tableIndexComparator = getComparator(SC_SORT_ALPHABETICALLY_TABLE_INDICES,
                                         config);
    procedureColumnComparator = getComparator(SC_SORT_ALPHABETICALLY_PROCEDURE_COLUMNS,
                                              config);
  }

  public InclusionRule getColumnInclusionRule()
  {
    return columnInclusionRule;
  }

  public InclusionRule getTableInclusionRule()
  {
    return tableInclusionRule;
  }

  /**
   * Get the table types.
   * 
   * @return Table types
   */
  public TableType[] getTableTypes()
  {
    final TableType[] tableTypesCopy = copyTableTypes(tableTypes);
    return tableTypesCopy;
  }

  public boolean isAlphabeticalSortForForeignKeys()
  {
    return tableForeignKeyComparator != null
           && tableForeignKeyComparator instanceof AlphabeticalSortComparator;
  }

  public boolean isAlphabeticalSortForIndexes()
  {
    return tableIndexComparator != null
           && tableIndexComparator instanceof AlphabeticalSortComparator;
  }

  public boolean isAlphabeticalSortForProcedureColumns()
  {
    return procedureColumnComparator != null
           && procedureColumnComparator instanceof AlphabeticalSortComparator;
  }

  public boolean isAlphabeticalSortForTableColumns()
  {
    return tableColumnComparator != null
           && tableColumnComparator instanceof AlphabeticalSortComparator;
  }

  public boolean isShowStoredProcedures()
  {
    return showStoredProcedures;
  }

  public void setAlphabeticalSortForForeignKeys(final boolean alphabeticalSort)
  {
    tableForeignKeyComparator = getComparator(alphabeticalSort);
  }

  public void setAlphabeticalSortForIndexes(final boolean alphabeticalSort)
  {
    tableIndexComparator = getComparator(alphabeticalSort);
  }

  public void setAlphabeticalSortForProcedureColumns(final boolean alphabeticalSort)
  {
    procedureColumnComparator = getComparator(alphabeticalSort);
  }

  public void setAlphabeticalSortForTableColumns(final boolean alphabeticalSort)
  {
    tableColumnComparator = getComparator(alphabeticalSort);
  }

  public void setColumnInclusionRule(final InclusionRule columnInclusionRule)
  {
    this.columnInclusionRule = columnInclusionRule;
  }

  /**
   * Set show stored procedures.
   * 
   * @param showStoredProcedures
   *        Show stored procedures
   */
  public void setShowStoredProcedures(final boolean showStoredProcedures)
  {
    this.showStoredProcedures = showStoredProcedures;
  }

  public void setTableInclusionRule(final InclusionRule tableInclusionRule)
  {
    this.tableInclusionRule = tableInclusionRule;
  }

  /**
   * Sets table types from a comma-separated list of table types. For
   * example:
   * TABLE,VIEW,SYSTEM_TABLE,GLOBAL_TEMPORARY,LOCAL_TEMPORARY,ALIAS,SYNONYM
   * 
   * @param tableTypes
   *        Comma-separated list of table types.
   */
  public void setTableTypes(final String tableTypesString)
  {
    tableTypes = TableType.valueOf(tableTypesString.split(","));
  }

  /**
   * Sets table types from an array of table types.
   * 
   * @param tableTypes
   *        Array of table types.
   */
  public void setTableTypes(final TableType[] tableTypesArray)
  {
    tableTypes = copyTableTypes(tableTypesArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @see Object#toString()
   */
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("SchemaCrawlerOptions[");
    buffer.append("tableInclusionRule=").append(tableInclusionRule);
    buffer.append(", columnInclusionRule=").append(columnInclusionRule);
    buffer.append(", showStoredProcedures=").append(showStoredProcedures);
    if (tableTypes == null)
    {
      buffer.append(", tableTypes=").append("null");
    }
    else
    {
      buffer.append(", tableTypes=").append(Arrays.asList(tableTypes)
        .toString());
    }
    buffer.append("]");
    return buffer.toString();
  }

  private SerializableComparator getComparator(final boolean alphabeticalSort)
  {
    if (alphabeticalSort)
    {
      return new AlphabeticalSortComparator();
    }
    else
    {
      return new NaturalSortComparator();
    }
  }

  private SerializableComparator getComparator(final String propertyName,
                                               final Properties config)
  {
    return getComparator(getBooleanProperty(propertyName, config));
  }

  SerializableComparator getProcedureColumnComparator()
  {
    return procedureColumnComparator;
  }

  SerializableComparator getTableColumnComparator()
  {
    return tableColumnComparator;
  }

  SerializableComparator getTableForeignKeyComparator()
  {
    return tableForeignKeyComparator;
  }

  SerializableComparator getTableIndexComparator()
  {
    return tableIndexComparator;
  }

}

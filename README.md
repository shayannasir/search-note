# Search Note

**Search Note** is a light-weight, super-fast, keyboard-shortcuts-centric, search-oriented note-taking/fetching desktop application.

All the actions in *search-note* are performed via keyboard shortcuts.

## Structure

Search Note has 2 stages/layouts, specifically:-

1. **Table:**: Tables are the main entry points to a document. Analogous to a collection inside a Document based Database, a single Table may hold numerous *Entries*
2. **Entry**: Entries exist within a particular table and have 3 attributes in them - key, value and description.

## Features

1. User Registration and login
2. Document oriented architecture (tables -> entries)
3. Keyboard shortcuts for everything
4. Easy clipboard operations
5. Note Archiving 

## Portability

As of it's first release, Search Note can be downloaded as a jar (recommended) or a .deb package (beta stage) . With time, more OS-specific executables of the application will be released.

### Development

Search Note in under active development.

## Build

1. Clone project
2. Ensure the correct javafx support version of java 11 is set up (11.0.9.fx recommended)
3. Run ``mvn clean install``
4. Find and run the generated .jar (/target) or .deb (/debian/src)
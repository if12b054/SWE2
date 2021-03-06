USE [master]
GO
/****** Object:  Database [ErpDB]    Script Date: 08.05.2014 11:41:11 ******/
CREATE DATABASE [ErpDB]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'ErpDB', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS\MSSQL\DATA\ErpDB.mdf' , SIZE = 4160KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'ErpDB_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.SQLEXPRESS\MSSQL\DATA\ErpDB_log.ldf' , SIZE = 1040KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [ErpDB] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ErpDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ErpDB] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ErpDB] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ErpDB] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ErpDB] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ErpDB] SET ARITHABORT OFF 
GO
ALTER DATABASE [ErpDB] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ErpDB] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [ErpDB] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ErpDB] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ErpDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ErpDB] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ErpDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ErpDB] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ErpDB] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ErpDB] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ErpDB] SET  DISABLE_BROKER 
GO
ALTER DATABASE [ErpDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ErpDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ErpDB] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ErpDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ErpDB] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ErpDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ErpDB] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ErpDB] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [ErpDB] SET  MULTI_USER 
GO
ALTER DATABASE [ErpDB] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ErpDB] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ErpDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ErpDB] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
USE [ErpDB]
GO
/****** Object:  User [TestU]    Script Date: 08.05.2014 11:41:11 ******/
CREATE USER [TestU] FOR LOGIN [Test] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [TestU]
GO
/****** Object:  Table [dbo].[Adresse]    Script Date: 08.05.2014 11:41:11 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Adresse](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Straße] [varchar](50) NULL,
	[PLZ] [int] NULL,
	[Ort] [varchar](30) NULL,
	[Land] [varchar](30) NULL,
 CONSTRAINT [PK_Adresse] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Artikel]    Script Date: 08.05.2014 11:41:11 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Artikel](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Bezeichnung] [varchar](50) NULL,
	[PreisNetto] [float] NULL,
 CONSTRAINT [PK_Artikel] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Kontakt]    Script Date: 08.05.2014 11:41:11 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Kontakt](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[UID] [int] NULL,
	[Firmenname] [varchar](30) NULL,
	[Titel] [varchar](15) NULL,
	[Vorname] [varchar](25) NULL,
	[Nachname] [varchar](25) NULL,
	[Geburtsdatum] [date] NULL,
	[Adresse] [int] NULL,
	[Typ] [varchar](10) NULL,
 CONSTRAINT [PK__Kontakt__3214EC27BF6C4469] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Rechnung]    Script Date: 08.05.2014 11:41:11 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Rechnung](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Kunde_ID] [int] NULL,
	[Datum] [date] NULL,
	[Fälligkeit] [date] NULL,
	[Kommentar] [varchar](100) NULL,
	[Nachricht] [varchar](100) NULL,
	[MWSt] [float] NULL,
	[Netto] [float] NULL,
	[Brutto] [float] NULL,
	[Rechnungsadresse] [int] NULL,
	[Lieferadresse] [int] NULL,
 CONSTRAINT [PK__Rechnung__3214EC278CC56FD2] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Rechnungszeilen]    Script Date: 08.05.2014 11:41:11 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Rechnungszeilen](
	[Rechnung_ID] [int] NOT NULL,
	[Artikel_ID] [int] NOT NULL,
	[Menge] [int] NULL,
 CONSTRAINT [PK_Rechnungspositionen] PRIMARY KEY CLUSTERED 
(
	[Rechnung_ID] ASC,
	[Artikel_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET IDENTITY_INSERT [dbo].[Artikel] ON 

INSERT [dbo].[Artikel] ([ID], [Bezeichnung], [PreisNetto]) VALUES (1, N'Lolly', 1.12)
INSERT [dbo].[Artikel] ([ID], [Bezeichnung], [PreisNetto]) VALUES (2, N'Laterne', 4.23)
INSERT [dbo].[Artikel] ([ID], [Bezeichnung], [PreisNetto]) VALUES (3, N'Lastwagen', 34068.1)
INSERT [dbo].[Artikel] ([ID], [Bezeichnung], [PreisNetto]) VALUES (4, N'Laufhaus', 534300)
INSERT [dbo].[Artikel] ([ID], [Bezeichnung], [PreisNetto]) VALUES (5, N'Laktosefreie Milch', 1.77)
SET IDENTITY_INSERT [dbo].[Artikel] OFF
SET IDENTITY_INSERT [dbo].[Kontakt] ON 

INSERT [dbo].[Kontakt] ([ID], [UID], [Firmenname], [Titel], [Vorname], [Nachname], [Geburtsdatum], [Adresse], [Typ]) VALUES (1, NULL, NULL, N'Herr', N'Victor', N'Stabrawa', CAST(0x22250B00 AS Date), NULL, N'Person')
SET IDENTITY_INSERT [dbo].[Kontakt] OFF
ALTER TABLE [dbo].[Kontakt]  WITH CHECK ADD  CONSTRAINT [FK_Kontakt_Rechnungsadresse] FOREIGN KEY([Adresse])
REFERENCES [dbo].[Adresse] ([ID])
GO
ALTER TABLE [dbo].[Kontakt] CHECK CONSTRAINT [FK_Kontakt_Rechnungsadresse]
GO
ALTER TABLE [dbo].[Rechnung]  WITH CHECK ADD  CONSTRAINT [FK__Rechnung__K_ID__1273C1CD] FOREIGN KEY([Kunde_ID])
REFERENCES [dbo].[Kontakt] ([ID])
GO
ALTER TABLE [dbo].[Rechnung] CHECK CONSTRAINT [FK__Rechnung__K_ID__1273C1CD]
GO
ALTER TABLE [dbo].[Rechnung]  WITH CHECK ADD  CONSTRAINT [FK_Rechnung_Adresse] FOREIGN KEY([Lieferadresse])
REFERENCES [dbo].[Adresse] ([ID])
GO
ALTER TABLE [dbo].[Rechnung] CHECK CONSTRAINT [FK_Rechnung_Adresse]
GO
ALTER TABLE [dbo].[Rechnung]  WITH CHECK ADD  CONSTRAINT [FK_Rechnung_Adresse1] FOREIGN KEY([Rechnungsadresse])
REFERENCES [dbo].[Adresse] ([ID])
GO
ALTER TABLE [dbo].[Rechnung] CHECK CONSTRAINT [FK_Rechnung_Adresse1]
GO
ALTER TABLE [dbo].[Rechnungszeilen]  WITH CHECK ADD  CONSTRAINT [FK_Rechnungspositionen_Artikel] FOREIGN KEY([Artikel_ID])
REFERENCES [dbo].[Artikel] ([ID])
GO
ALTER TABLE [dbo].[Rechnungszeilen] CHECK CONSTRAINT [FK_Rechnungspositionen_Artikel]
GO
ALTER TABLE [dbo].[Rechnungszeilen]  WITH CHECK ADD  CONSTRAINT [FK_Rechnungspositionen_Rechnung] FOREIGN KEY([Rechnung_ID])
REFERENCES [dbo].[Rechnung] ([ID])
GO
ALTER TABLE [dbo].[Rechnungszeilen] CHECK CONSTRAINT [FK_Rechnungspositionen_Rechnung]
GO
USE [master]
GO
ALTER DATABASE [ErpDB] SET  READ_WRITE 
GO


import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class ifdField {
    int tag;
    int type;
    long count; 
    long offset;
    HashMap<Integer, String> tagDesc;
    HashMap<Integer, String> typeDesc;


    public ifdField() {
        this.tagDesc = new HashMap<>();
/*
tagDesc.put(	254	, "	NewSubfileType      "); //must
tagDesc.put(	255	, "	SubfileType         ");
tagDesc.put(	256	, "	ImageWidth          "); //must
tagDesc.put(	257	, "	ImageLength         "); //must
tagDesc.put(	258	, "	BitsPerSample       "); //must
tagDesc.put(	259	, "	Compression         "); //must
tagDesc.put(	262	, "	PhotometricInterpretation	"); //must
tagDesc.put(	263	, "	Threshholding       ");
tagDesc.put(	264	, "	CellWidth           ");
tagDesc.put(	265	, "	CellLength          ");
tagDesc.put(	266	, "	FillOrder           ");
tagDesc.put(	270	, "	ImageDescription    ");
tagDesc.put(	271	, "	Make                ");
tagDesc.put(	272	, "	Model               ");
tagDesc.put(	273	, "	StripOffsets        "); //must
tagDesc.put(	274	, "	Orientation         ");
tagDesc.put(	277	, "	SamplesPerPixel     "); //must
tagDesc.put(	278	, "	RowsPerStrip        "); //must
tagDesc.put(	279	, "	StripByteCounts     "); //must
tagDesc.put(	280	, "	MinSampleValue      ");
tagDesc.put(	281	, "	MaxSampleValue      ");
tagDesc.put(	282	, "	XResolution         ");
tagDesc.put(	283	, "	YResolution         ");
tagDesc.put(	284	, "	PlanarConfiguration ");
tagDesc.put(	288	, "	FreeOffsets         ");
tagDesc.put(	289	, "	FreeByteCounts      ");
tagDesc.put(	290	, "	GrayResponseUnit    ");
tagDesc.put(	291	, "	GrayResponseCurve   ");
tagDesc.put(	296	, "	ResolutionUnit      ");
tagDesc.put(	305	, "	Software            ");
tagDesc.put(	306	, "	DateTime            ");
tagDesc.put(	315	, "	Artist              ");
tagDesc.put(	316	, "	HostComputer        ");
tagDesc.put(	320	, "	ColorMap            ");
tagDesc.put(	330	, "	SubIFDs            ");
tagDesc.put(	338	, "	ExtraSamples        ");
tagDesc.put(	33432	, "     Copyright           ");
*/

tagDesc.put(	254	, "	NewSubfileType	");
tagDesc.put(	255	, "	SubfileType	");
tagDesc.put(	256	, "	ImageWidth	");
tagDesc.put(	257	, "	ImageLength	");
tagDesc.put(	258	, "	BitsPerSample	");
tagDesc.put(	259	, "	Compression	");
tagDesc.put(	262	, "	PhotometricInterpretation	");
tagDesc.put(	263	, "	Threshholding	");
tagDesc.put(	264	, "	CellWidth	");
tagDesc.put(	265	, "	CellLength	");
tagDesc.put(	266	, "	FillOrder	");
tagDesc.put(	269	, "	DocumentName	");
tagDesc.put(	270	, "	ImageDescription	");
tagDesc.put(	271	, "	Make	");
tagDesc.put(	272	, "	Model	");
tagDesc.put(	273	, "	StripOffsets	");
tagDesc.put(	274	, "	Orientation	");
tagDesc.put(	277	, "	SamplesPerPixel	");
tagDesc.put(	278	, "	RowsPerStrip	");
tagDesc.put(	279	, "	StripByteCounts	");
tagDesc.put(	280	, "	MinSampleValue	");
tagDesc.put(	281	, "	MaxSampleValue	");
tagDesc.put(	282	, "	XResolution	");
tagDesc.put(	283	, "	YResolution	");
tagDesc.put(	284	, "	PlanarConfiguration	");
tagDesc.put(	285	, "	PageName	");
tagDesc.put(	286	, "	XPosition	");
tagDesc.put(	287	, "	YPosition	");
tagDesc.put(	288	, "	FreeOffsets	");
tagDesc.put(	289	, "	FreeByteCounts	");
tagDesc.put(	290	, "	GrayResponseUnit	");
tagDesc.put(	291	, "	GrayResponseCurve	");
tagDesc.put(	292	, "	T4Options	");
tagDesc.put(	293	, "	T6Options	");
tagDesc.put(	296	, "	ResolutionUnit	");
tagDesc.put(	297	, "	PageNumber	");
tagDesc.put(	301	, "	TransferFunction	");
tagDesc.put(	305	, "	Software	");
tagDesc.put(	306	, "	DateTime	");
tagDesc.put(	315	, "	Artist	");
tagDesc.put(	316	, "	HostComputer	");
tagDesc.put(	317	, "	Predictor	");
tagDesc.put(	318	, "	WhitePoint	");
tagDesc.put(	319	, "	PrimaryChromaticities	");
tagDesc.put(	320	, "	ColorMap	");
tagDesc.put(	321	, "	HalftoneHints	");
tagDesc.put(	322	, "	TileWidth	");
tagDesc.put(	323	, "	TileLength	");
tagDesc.put(	324	, "	TileOffsets	");
tagDesc.put(	325	, "	TileByteCounts	");
tagDesc.put(	326	, "	BadFaxLines	");
tagDesc.put(	327	, "	CleanFaxData	");
tagDesc.put(	328	, "	ConsecutiveBadFaxLines	");
tagDesc.put(	330	, "	SubIFDs	");
tagDesc.put(	332	, "	InkSet	");
tagDesc.put(	333	, "	InkNames	");
tagDesc.put(	334	, "	NumberOfInks	");
tagDesc.put(	336	, "	DotRange	");
tagDesc.put(	337	, "	TargetPrinter	");
tagDesc.put(	338	, "	ExtraSamples	");
tagDesc.put(	339	, "	SampleFormat	");
tagDesc.put(	340	, "	SMinSampleValue	");
tagDesc.put(	341	, "	SMaxSampleValue	");
tagDesc.put(	342	, "	TransferRange	");
tagDesc.put(	343	, "	ClipPath	");
tagDesc.put(	344	, "	XClipPathUnits	");
tagDesc.put(	345	, "	YClipPathUnits	");
tagDesc.put(	346	, "	Indexed	");
tagDesc.put(	347	, "	JPEGTables	");
tagDesc.put(	351	, "	OPIProxy	");
tagDesc.put(	400	, "	GlobalParametersIFD	");
tagDesc.put(	401	, "	ProfileType	");
tagDesc.put(	402	, "	FaxProfile	");
tagDesc.put(	403	, "	CodingMethods	");
tagDesc.put(	404	, "	VersionYear	");
tagDesc.put(	405	, "	ModeNumber	");
tagDesc.put(	433	, "	Decode	");
tagDesc.put(	434	, "	DefaultImageColor	");
tagDesc.put(	512	, "	JPEGProc	");
tagDesc.put(	513	, "	JPEGInterchangeFormat	");
tagDesc.put(	514	, "	JPEGInterchangeFormatLength	");
tagDesc.put(	515	, "	JPEGRestartInterval	");
tagDesc.put(	517	, "	JPEGLosslessPredictors	");
tagDesc.put(	518	, "	JPEGPointTransforms	");
tagDesc.put(	519	, "	JPEGQTables	");
tagDesc.put(	520	, "	JPEGDCTables	");
tagDesc.put(	521	, "	JPEGACTables	");
tagDesc.put(	529	, "	YCbCrCoefficients	");
tagDesc.put(	530	, "	YCbCrSubSampling	");
tagDesc.put(	531	, "	YCbCrPositioning	");
tagDesc.put(	532	, "	ReferenceBlackWhite	");
tagDesc.put(	559	, "	StripRowCounts	");
tagDesc.put(	700	, "	XMP	");
tagDesc.put(	18246	, "	Image.Rating	");
tagDesc.put(	18249	, "	Image.RatingPercent	");
tagDesc.put(	32781	, "	ImageID	");
tagDesc.put(	32932	, "	Wang Annotation	");
tagDesc.put(	33421	, "	CFARepeatPatternDim	");
tagDesc.put(	33422	, "	CFAPattern	");
tagDesc.put(	33423	, "	BatteryLevel	");
tagDesc.put(	33432	, "	Copyright	");
tagDesc.put(	33434	, "	ExposureTime	");
tagDesc.put(	33437	, "	FNumber	");
tagDesc.put(	33445	, "	MD FileTag	");
tagDesc.put(	33446	, "	MD ScalePixel	");
tagDesc.put(	33447	, "	MD ColorTable	");
tagDesc.put(	33448	, "	MD LabName	");
tagDesc.put(	33449	, "	MD SampleInfo	");
tagDesc.put(	33450	, "	MD PrepDate	");
tagDesc.put(	33451	, "	MD PrepTime	");
tagDesc.put(	33452	, "	MD FileUnits	");
tagDesc.put(	33550	, "	ModelPixelScaleTag	");
tagDesc.put(	33723	, "	IPTC/NAA	");
tagDesc.put(	33918	, "	INGR Packet Data Tag	");
tagDesc.put(	33919	, "	INGR Flag Registers	");
tagDesc.put(	33920	, "	IrasB Transformation Matrix	");
tagDesc.put(	33922	, "	ModelTiepointTag	");
tagDesc.put(	34016	, "	Site	");
tagDesc.put(	34017	, "	ColorSequence	");
tagDesc.put(	34018	, "	IT8Header	");
tagDesc.put(	34019	, "	RasterPadding	");
tagDesc.put(	34020	, "	BitsPerRunLength	");
tagDesc.put(	34021	, "	BitsPerExtendedRunLength	");
tagDesc.put(	34022	, "	ColorTable	");
tagDesc.put(	34023	, "	ImageColorIndicator	");
tagDesc.put(	34024	, "	BackgroundColorIndicator	");
tagDesc.put(	34025	, "	ImageColorValue	");
tagDesc.put(	34026	, "	BackgroundColorValue	");
tagDesc.put(	34027	, "	PixelIntensityRange	");
tagDesc.put(	34028	, "	TransparencyIndicator	");
tagDesc.put(	34029	, "	ColorCharacterization	");
tagDesc.put(	34030	, "	HCUsage	");
tagDesc.put(	34031	, "	TrapIndicator	");
tagDesc.put(	34032	, "	CMYKEquivalent	");
tagDesc.put(	34033	, "	Reserved	");
tagDesc.put(	34034	, "	Reserved	");
tagDesc.put(	34035	, "	Reserved	");
tagDesc.put(	34264	, "	ModelTransformationTag	");
tagDesc.put(	34377	, "	Photoshop	");
tagDesc.put(	34665	, "	Exif IFD	");
tagDesc.put(	34675	, "	InterColorProfile	");
tagDesc.put(	34732	, "	ImageLayer	");
tagDesc.put(	34735	, "	GeoKeyDirectoryTag	");
tagDesc.put(	34736	, "	GeoDoubleParamsTag	");
tagDesc.put(	34737	, "	GeoAsciiParamsTag	");
tagDesc.put(	34850	, "	ExposureProgram	");
tagDesc.put(	34852	, "	SpectralSensitivity	");
tagDesc.put(	34853	, "	GPSInfo	");
tagDesc.put(	34855	, "	ISOSpeedRatings	");
tagDesc.put(	34856	, "	OECF	");
tagDesc.put(	34857	, "	Interlace	");
tagDesc.put(	34858	, "	TimeZoneOffset	");
tagDesc.put(	34859	, "	SelfTimeMode	");
tagDesc.put(	34864	, "	SensitivityType	");
tagDesc.put(	34865	, "	StandardOutputSensitivity	");
tagDesc.put(	34866	, "	RecommendedExposureIndex	");
tagDesc.put(	34867	, "	ISOSpeed	");
tagDesc.put(	34868	, "	ISOSpeedLatitudeyyy	");
tagDesc.put(	34869	, "	ISOSpeedLatitudezzz	");
tagDesc.put(	34908	, "	HylaFAX FaxRecvParams	");
tagDesc.put(	34909	, "	HylaFAX FaxSubAddress	");
tagDesc.put(	34910	, "	HylaFAX FaxRecvTime	");
tagDesc.put(	36864	, "	ExifVersion	");
tagDesc.put(	36867	, "	DateTimeOriginal	");
tagDesc.put(	36868	, "	DateTimeDigitized	");
tagDesc.put(	37121	, "	ComponentsConfiguration	");
tagDesc.put(	37122	, "	CompressedBitsPerPixel	");
tagDesc.put(	37377	, "	ShutterSpeedValue	");
tagDesc.put(	37378	, "	ApertureValue	");
tagDesc.put(	37379	, "	BrightnessValue	");
tagDesc.put(	37380	, "	ExposureBiasValue	");
tagDesc.put(	37381	, "	MaxApertureValue	");
tagDesc.put(	37382	, "	SubjectDistance	");
tagDesc.put(	37383	, "	MeteringMode	");
tagDesc.put(	37384	, "	LightSource	");
tagDesc.put(	37385	, "	Flash	");
tagDesc.put(	37386	, "	FocalLength	");
tagDesc.put(	37387	, "	FlashEnergy	");
tagDesc.put(	37388	, "	SpatialFrequencyResponse	");
tagDesc.put(	37389	, "	Noise	");
tagDesc.put(	37390	, "	FocalPlaneXResolution	");
tagDesc.put(	37391	, "	FocalPlaneYResolution	");
tagDesc.put(	37392	, "	FocalPlaneResolutionUnit	");
tagDesc.put(	37393	, "	ImageNumber	");
tagDesc.put(	37394	, "	SecurityClassification	");
tagDesc.put(	37395	, "	ImageHistory	");
tagDesc.put(	37396	, "	SubjectLocation	");
tagDesc.put(	37397	, "	ExposureIndex	");
tagDesc.put(	37398	, "	TIFF/EPStandardID	");
tagDesc.put(	37399	, "	SensingMethod	");
tagDesc.put(	37500	, "	MakerNote	");
tagDesc.put(	37510	, "	UserComment	");
tagDesc.put(	37520	, "	SubsecTime	");
tagDesc.put(	37521	, "	SubsecTimeOriginal	");
tagDesc.put(	37522	, "	SubsecTimeDigitized	");
tagDesc.put(	37724	, "	ImageSourceData	");
tagDesc.put(	40091	, "	XPTitle	");
tagDesc.put(	40092	, "	XPComment	");
tagDesc.put(	40093	, "	XPAuthor	");
tagDesc.put(	40094	, "	XPKeywords	");
tagDesc.put(	40095	, "	XPSubject	");
tagDesc.put(	40960	, "	FlashpixVersion	");
tagDesc.put(	40961	, "	ColorSpace	");
tagDesc.put(	40962	, "	PixelXDimension	");
tagDesc.put(	40963	, "	PixelYDimension	");
tagDesc.put(	40964	, "	RelatedSoundFile	");
tagDesc.put(	40965	, "	Interoperability IFD	");
tagDesc.put(	41483	, "	FlashEnergy	");
tagDesc.put(	41484	, "	SpatialFrequencyResponse	");
tagDesc.put(	41486	, "	FocalPlaneXResolution	");
tagDesc.put(	41487	, "	FocalPlaneYResolution	");
tagDesc.put(	41488	, "	FocalPlaneResolutionUnit	");
tagDesc.put(	41492	, "	SubjectLocation	");
tagDesc.put(	41493	, "	ExposureIndex	");
tagDesc.put(	41495	, "	SensingMethod	");
tagDesc.put(	41728	, "	FileSource	");
tagDesc.put(	41729	, "	SceneType	");
tagDesc.put(	41730	, "	CFAPattern	");
tagDesc.put(	41985	, "	CustomRendered	");
tagDesc.put(	41986	, "	ExposureMode	");
tagDesc.put(	41987	, "	WhiteBalance	");
tagDesc.put(	41988	, "	DigitalZoomRatio	");
tagDesc.put(	41989	, "	FocalLengthIn35mmFilm	");
tagDesc.put(	41990	, "	SceneCaptureType	");
tagDesc.put(	41991	, "	GainControl	");
tagDesc.put(	41992	, "	Contrast	");
tagDesc.put(	41993	, "	Saturation	");
tagDesc.put(	41994	, "	Sharpness	");
tagDesc.put(	41995	, "	DeviceSettingDescription	");
tagDesc.put(	41996	, "	SubjectDistanceRange	");
tagDesc.put(	42016	, "	ImageUniqueID	");
tagDesc.put(	42032	, "	CameraOwnerName	");
tagDesc.put(	42033	, "	BodySerialNumber	");
tagDesc.put(	42034	, "	LensSpecification	");
tagDesc.put(	42035	, "	LensMake	");
tagDesc.put(	42036	, "	LensModel	");
tagDesc.put(	42037	, "	LensSerialNumber	");
tagDesc.put(	42112	, "	GDAL_METADATA	");
tagDesc.put(	42113	, "	GDAL_NODATA	");
tagDesc.put(	48129	, "	PixelFormat	");
tagDesc.put(	48130	, "	Transformation	");
tagDesc.put(	48131	, "	Uncompressed	");
tagDesc.put(	48132	, "	ImageType	");
tagDesc.put(	48132	, "	ImageType	");
tagDesc.put(	48256	, "	ImageWidth	");
tagDesc.put(	48257	, "	ImageHeight	");
tagDesc.put(	48258	, "	WidthResolution	");
tagDesc.put(	48259	, "	HeightResolution	");
tagDesc.put(	48320	, "	ImageOffset	");
tagDesc.put(	48321	, "	ImageByteCount	");
tagDesc.put(	48322	, "	AlphaOffset	");
tagDesc.put(	48323	, "	AlphaByteCount	");
tagDesc.put(	48324	, "	ImageDataDiscard	");
tagDesc.put(	48325	, "	AlphaDataDiscard	");
tagDesc.put(	50215	, "	Oce Scanjob Description	");
tagDesc.put(	50216	, "	Oce Application Selector	");
tagDesc.put(	50217	, "	Oce Identification Number	");
tagDesc.put(	50218	, "	Oce ImageLogic Characteristics	");
tagDesc.put(	50341	, "	PrintImageMatching	");
tagDesc.put(	50706	, "	DNGVersion	");
tagDesc.put(	50707	, "	DNGBackwardVersion	");
tagDesc.put(	50708	, "	UniqueCameraModel	");
tagDesc.put(	50709	, "	LocalizedCameraModel	");
tagDesc.put(	50710	, "	CFAPlaneColor	");
tagDesc.put(	50711	, "	CFALayout	");
tagDesc.put(	50712	, "	LinearizationTable	");
tagDesc.put(	50713	, "	BlackLevelRepeatDim	");
tagDesc.put(	50714	, "	BlackLevel	");
tagDesc.put(	50715	, "	BlackLevelDeltaH	");
tagDesc.put(	50716	, "	BlackLevelDeltaV	");
tagDesc.put(	50717	, "	WhiteLevel	");
tagDesc.put(	50718	, "	DefaultScale	");
tagDesc.put(	50719	, "	DefaultCropOrigin	");
tagDesc.put(	50720	, "	DefaultCropSize	");
tagDesc.put(	50721	, "	ColorMatrix1	");
tagDesc.put(	50722	, "	ColorMatrix2	");
tagDesc.put(	50723	, "	CameraCalibration1	");
tagDesc.put(	50724	, "	CameraCalibration2	");
tagDesc.put(	50725	, "	ReductionMatrix1	");
tagDesc.put(	50726	, "	ReductionMatrix2	");
tagDesc.put(	50727	, "	AnalogBalance	");
tagDesc.put(	50728	, "	AsShotNeutral	");
tagDesc.put(	50729	, "	AsShotWhiteXY	");
tagDesc.put(	50730	, "	BaselineExposure	");
tagDesc.put(	50731	, "	BaselineNoise	");
tagDesc.put(	50732	, "	BaselineSharpness	");
tagDesc.put(	50733	, "	BayerGreenSplit	");
tagDesc.put(	50734	, "	LinearResponseLimit	");
tagDesc.put(	50735	, "	CameraSerialNumber	");
tagDesc.put(	50736	, "	LensInfo	");
tagDesc.put(	50737	, "	ChromaBlurRadius	");
tagDesc.put(	50738	, "	AntiAliasStrength	");
tagDesc.put(	50739	, "	ShadowScale	");
tagDesc.put(	50740	, "	DNGPrivateData	");
tagDesc.put(	50741	, "	MakerNoteSafety	");
tagDesc.put(	50778	, "	CalibrationIlluminant1	");
tagDesc.put(	50779	, "	CalibrationIlluminant2	");
tagDesc.put(	50780	, "	BestQualityScale	");
tagDesc.put(	50781	, "	RawDataUniqueID	");
tagDesc.put(	50784	, "	Alias Layer Metadata	");
tagDesc.put(	50827	, "	OriginalRawFileName	");
tagDesc.put(	50828	, "	OriginalRawFileData	");
tagDesc.put(	50829	, "	ActiveArea	");
tagDesc.put(	50830	, "	MaskedAreas	");
tagDesc.put(	50831	, "	AsShotICCProfile	");
tagDesc.put(	50832	, "	AsShotPreProfileMatrix	");
tagDesc.put(	50833	, "	CurrentICCProfile	");
tagDesc.put(	50834	, "	CurrentPreProfileMatrix	");
tagDesc.put(	50879	, "	ColorimetricReference	");
tagDesc.put(	50931	, "	CameraCalibrationSignature	");
tagDesc.put(	50932	, "	ProfileCalibrationSignature	");
tagDesc.put(	50933	, "	ExtraCameraProfiles	");
tagDesc.put(	50934	, "	AsShotProfileName	");
tagDesc.put(	50935	, "	NoiseReductionApplied	");
tagDesc.put(	50936	, "	ProfileName	");
tagDesc.put(	50937	, "	ProfileHueSatMapDims	");
tagDesc.put(	50938	, "	ProfileHueSatMapData1	");
tagDesc.put(	50939	, "	ProfileHueSatMapData2	");
tagDesc.put(	50940	, "	ProfileToneCurve	");
tagDesc.put(	50941	, "	ProfileEmbedPolicy	");
tagDesc.put(	50942	, "	ProfileCopyright	");
tagDesc.put(	50964	, "	ForwardMatrix1	");
tagDesc.put(	50965	, "	ForwardMatrix2	");
tagDesc.put(	50966	, "	PreviewApplicationName	");
tagDesc.put(	50967	, "	PreviewApplicationVersion	");
tagDesc.put(	50968	, "	PreviewSettingsName	");
tagDesc.put(	50969	, "	PreviewSettingsDigest	");
tagDesc.put(	50970	, "	PreviewColorSpace	");
tagDesc.put(	50971	, "	PreviewDateTime	");
tagDesc.put(	50972	, "	RawImageDigest	");
tagDesc.put(	50973	, "	OriginalRawFileDigest	");
tagDesc.put(	50974	, "	SubTileBlockSize	");
tagDesc.put(	50975	, "	RowInterleaveFactor	");
tagDesc.put(	50981	, "	ProfileLookTableDims	");
tagDesc.put(	50982	, "	ProfileLookTableData	");
tagDesc.put(	51008	, "	OpcodeList1	");
tagDesc.put(	51009	, "	OpcodeList2	");
tagDesc.put(	51022	, "	OpcodeList3	");
tagDesc.put(	51041	, "	NoiseProfile	");
tagDesc.put(	51089	, "	OriginalDefaultFinalSize	");
tagDesc.put(	51090	, "	OriginalBestQualityFinalSize	");
tagDesc.put(	51091	, "	OriginalDefaultCropSize	");
tagDesc.put(	51107	, "	ProfileHueSatMapEncoding	");
tagDesc.put(	51108	, "	ProfileLookTableEncoding	");
tagDesc.put(	51109	, "	BaselineExposureOffset	");
tagDesc.put(	51110	, "	DefaultBlackRender	");
tagDesc.put(	51111	, "	NewRawImageDigest	");
tagDesc.put(	51112	, "	RawToPreviewGain	");
tagDesc.put(	51125	, "	DefaultUserCrop	");



        this.typeDesc = new HashMap<>();
typeDesc.put(1, "BYTE    "); //BYTE 8-bit unsigned integer.
typeDesc.put(2, "ASCII   "); // 8-bit byte that contains a 7-bit ASCII code; the last byte must be NUL (binary zero).
typeDesc.put(3, "SHORT   "); // 16-bit (2-byte) unsigned integer.
typeDesc.put(4, "LONG    "); // 32-bit (4-byte) unsigned integer.
typeDesc.put(5, "RATIONAL"); // Two LONGs: the first represents the numerator of a fraction; the second, the denominator.


    }
    
    
    
    public String getTag() {
        if (tagDesc.containsKey(tag))
            return tagDesc.get(tag);
        else return Integer.toString(tag);
    }

    public String getType() {
            return typeDesc.get(type);
    }
    
    public int getTypeLength() {
        switch (type) {
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 8;
            default:
                return 0;
        }
    }
    
    public long getCount() {
        return count;
    }
    
    public long getValue() {
        if (count < 4) return offset;
        else return -1;
    }
    
    public long getPointer() {
        if (count > 3) return offset;
        else return -1;
        
    }
    
}

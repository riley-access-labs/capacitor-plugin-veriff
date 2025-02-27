import Foundation
import Capacitor
import Veriff

@objc(VeriffPluginPlugin)
public class VeriffPluginPlugin: CAPPlugin {
    private var call: CAPPluginCall!
    
    @objc func start(_ call: CAPPluginCall) {
        
        self.call = call
        let sessionUrl = call.getString("sessionUrl") ?? ""
        var configuration = VeriffSdk.Configuration()
        
        if let veriffConfiguration: Dictionary<String,String> = call.getObject("configuration") as? Dictionary<String,String> {
            let branding = VeriffSdk.Branding()
            let url = URL(string: "https://firebasestorage.googleapis.com/v0/b/gwop-dev.appspot.com/o/public%2Fgwop_logo_dark.png?alt=media&token=131c18f6-e941-4564-83cd-90eea04580df")
            if let data = try? Data(contentsOf: url!) {
                branding.logo = UIImage(data: data)
            }
            branding.background = UIColor.init(hexString: veriffConfiguration["background"]!)
            branding.onBackground = UIColor.init(hexString: veriffConfiguration["onBackground"]!)
            branding.onBackgroundSecondary = UIColor.init(hexString: veriffConfiguration["onBackgroundSecondary"]!)
            branding.onBackgroundTertiary = UIColor.init(hexString: veriffConfiguration["onBackgroundTertiary"]!)
            branding.primary = UIColor.init(hexString: veriffConfiguration["primary"]!)
            branding.onPrimary = UIColor.init(hexString: veriffConfiguration["onPrimary"]!)
            branding.secondary = UIColor.init(hexString: veriffConfiguration["secondary"]!)
            branding.onSecondary = UIColor.init(hexString: veriffConfiguration["onSecondary"]!)
            branding.cameraOverlay = UIColor.init(hexString: veriffConfiguration["cameraOverlay"]!)
            branding.onCameraOverlay = UIColor.init(hexString: veriffConfiguration["onCameraOverlay"]!)
            branding.outline = UIColor.init(hexString: veriffConfiguration["outline"]!)
            branding.error = UIColor.init(hexString: veriffConfiguration["error"]!)
            branding.success = UIColor.init(hexString: veriffConfiguration["success"]!)
            branding.buttonRadius = CGFloat(8)
            branding.font = VeriffSdk.Branding.Font(regular: "Montserrat", medium: "Montserrat-Medium", bold: "Montserrat-Bold")
            configuration = VeriffSdk.Configuration(branding: branding)
        }
        
        let veriff = VeriffSdk.shared
        veriff.delegate = self
        DispatchQueue.main.async {
            veriff.startAuthentication(sessionUrl: sessionUrl, configuration: configuration, presentingFrom: (self.bridge?.viewController!)!)
        }
    }
}

extension VeriffPluginPlugin : VeriffSdkDelegate {
    public func sessionDidEndWithResult(_ result: VeriffSdk.Result) {
        var resultJson: Dictionary<String,String> = [:]
        switch result.status {
        case .done:
            // The user successfully submitted the session
            resultJson["status"] = "DONE"
            resultJson["message"] = "Session is completed from clients perspective"
            break
        case .canceled:
            // The user canceled the verification process.
            resultJson["status"] = "CANCELED"
            resultJson["message"] = "User canceled the verification process"
            break
        case .error(let error):
            resultJson["status"] = "ERROR"
            resultJson["message"] = VeriffSdk.Error.init(rawValue: error.rawValue).debugDescription
            
        @unknown default:
            print("Unknown result received from Veriff SDK")
            return self.call.reject("Unknown result received from Veriff SDK")
        }
        
        self.call.resolve(resultJson)
    }
}
